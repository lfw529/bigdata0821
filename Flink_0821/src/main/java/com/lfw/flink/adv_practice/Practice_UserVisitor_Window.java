package com.lfw.flink.adv_practice;

import com.lfw.flink.bean.UserBehavior;
import com.lfw.flink.bean.UserVisitorCount;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;

public class Practice_UserVisitor_Window {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.读取文本数据
        DataStreamSource<String> readTextFile = env.readTextFile("Flink_0821/src/main/resources/UserBehavior.csv");
        //3.转换为JavaBean,根据行为过滤数据，并提取时间戳生成Watermark
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

        //4.按照行为分组
        KeyedStream<UserBehavior, String> keyedStream = userBehaviorDS.keyBy(UserBehavior::getBehavior);

        //5.开窗
        WindowedStream<UserBehavior, String, TimeWindow> windowedStream = keyedStream.window(TumblingEventTimeWindows.of(Time.hours(1)));

        //6.使用HashSet方式
        SingleOutputStreamOperator<UserVisitorCount> result = windowedStream.process(new UserVisitorProcessWindowFunc());

        //7.打印并执行任务
        result.print();
        env.execute();
    }

    public static class UserVisitorProcessWindowFunc extends ProcessWindowFunction<UserBehavior, UserVisitorCount, String, TimeWindow> {

        @Override
        public void process(String s, Context context, Iterable<UserBehavior> elements, Collector<UserVisitorCount> out) throws Exception {
            //创建HashSet用于去重
            HashSet<Long> uids = new HashSet<>();

            //取出窗口中的所有数据
            Iterator<UserBehavior> iterator = elements.iterator();

            //遍历迭代器，将数据中的UID放入HashSet，去重
            while (iterator.hasNext()) {
                uids.add(iterator.next().getUserId());
            }
            //输出数据
            out.collect(new UserVisitorCount("UV", new Timestamp(context.window().getEnd()).toString(), uids.size()));
        }
    }
}
