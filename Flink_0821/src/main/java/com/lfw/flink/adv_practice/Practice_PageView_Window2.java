package com.lfw.flink.adv_practice;

import com.lfw.flink.bean.PageViewCount;
import com.lfw.flink.bean.UserBehavior;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Random;

public class Practice_PageView_Window2 {
    public static void main(String[] args) throws Exception {

        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //2.读取文本数据
        DataStreamSource<String> readTextFile = env.readTextFile("Flink_0821/src/main/resources/UserBehavior.csv");

        //3.转换为JavaBean,根据行为过滤数据,并提取时间戳生成Watermark
        WatermarkStrategy<UserBehavior> userBehaviorWatermarkStrategy = WatermarkStrategy.<UserBehavior>forMonotonousTimestamps()
                .withTimestampAssigner(new SerializableTimestampAssigner<UserBehavior>() {
                    @Override
                    public long extractTimestamp(UserBehavior element, long recordTimestamp) {
                        return element.getTimestamp() * 1000L;
                    }
                });
        SingleOutputStreamOperator<UserBehavior> userBehaviorDS = readTextFile.map(data -> {
            String[] split = data.split(",");
            return new UserBehavior(Long.parseLong(split[0]),
                    Long.parseLong(split[1]),
                    Integer.parseInt(split[2]),
                    split[3],
                    Long.parseLong(split[4]));
        }).filter(data -> "pv".equals(data.getBehavior()))
                .assignTimestampsAndWatermarks(userBehaviorWatermarkStrategy);

        //4.将数据转换为元组
        KeyedStream<Tuple2<String, Integer>, String> keyedStream = userBehaviorDS.map(new MapFunction<UserBehavior, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(UserBehavior value) throws Exception {
                //加了随机数打散，防止数据倾斜
                return new Tuple2<>("PV_" + new Random().nextInt(8), 1);
            }
        }).keyBy(data -> data.f0);

        //5.开窗并计算
        SingleOutputStreamOperator<PageViewCount> aggResult = keyedStream.window(TumblingEventTimeWindows.of(Time.hours(1)))
                .aggregate(new PageViewAggFunc(), new PageViewWindowFunc());

        //6.按照窗口信息重新分组做第二次聚合
        KeyedStream<PageViewCount, String> pageViewCountKeyedStream = aggResult.keyBy(PageViewCount::getTime);

        //7.累加结果,自定义函数，处理干扰数据，只显示最终结果
        SingleOutputStreamOperator<PageViewCount> result = pageViewCountKeyedStream.process(new PageViewProcessFunc());

        //8.执行任务
        result.print();
        env.execute();

    }

    public static class PageViewAggFunc implements AggregateFunction<Tuple2<String, Integer>, Integer, Integer> {
        @Override
        public Integer createAccumulator() {
            return 0;
        }

        @Override
        public Integer add(Tuple2<String, Integer> value, Integer accumulator) {
            return accumulator + 1;
        }

        @Override
        public Integer getResult(Integer accumulator) {
            return accumulator;
        }

        @Override
        public Integer merge(Integer a, Integer b) {
            return a + b;
        }
    }

    public static class PageViewWindowFunc implements WindowFunction<Integer, PageViewCount, String, TimeWindow> {

        @Override
        public void apply(String key, TimeWindow window, Iterable<Integer> input, Collector<PageViewCount> out) throws Exception {

            //提取窗口时间 [窗口的关闭时间]
            String timestamp = new Timestamp(window.getEnd()).toString();

            //获取累积结果
            Integer count = input.iterator().next();

            //输出结果
            out.collect(new PageViewCount("PV", timestamp, count));
        }
    }

    public static class PageViewProcessFunc extends KeyedProcessFunction<String, PageViewCount, PageViewCount> {

        //定义状态
        private ListState<PageViewCount> listState;

        @Override
        public void open(Configuration parameters) throws Exception {
            //获取上下文状态
            listState = getRuntimeContext().getListState(new ListStateDescriptor<PageViewCount>("list-state", PageViewCount.class));
        }

        @Override
        public void processElement(PageViewCount value, Context ctx, Collector<PageViewCount> out) throws Exception {
            //将数据放入状态
            listState.add(value);

            //注册定时器
            String time = value.getTime();
            long ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime();
            //设置1s的延迟
            ctx.timerService().registerEventTimeTimer(ts + 1);
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<PageViewCount> out) throws Exception {
            //取出状态中的数据
            Iterable<PageViewCount> pageViewCounts = listState.get();

            //遍历累加数据
            Integer count = 0;
            Iterator<PageViewCount> iterator = pageViewCounts.iterator();
            while (iterator.hasNext()) {
                PageViewCount next = iterator.next();
                count += next.getCount();
            }

            //输出数据   [减去1，回到窗口的关闭时间]
            out.collect(new PageViewCount("PV", new Timestamp(timestamp - 1).toString(), count));

            //清空状态
            listState.clear();
        }
    }
}
