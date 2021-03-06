package com.lfw.flink.window;

import org.apache.commons.compress.utils.Lists;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import java.sql.Timestamp;
import java.util.ArrayList;


public class Window_TimeTumbling {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //2.读取端口数据
        DataStreamSource<String> socketTextStream = env.socketTextStream("hadoop102", 7777);
        //3.压平并转换为元组
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordToOneDS = socketTextStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] words = value.split(" ");
                for (String word : words) {
                    out.collect(new Tuple2<>(word, 1));
                }
            }
        });
        //4.按照单词分组
        KeyedStream<Tuple2<String, Integer>, String> keyedStream = wordToOneDS.keyBy(data -> data.f0);
        //5.开窗
        WindowedStream<Tuple2<String, Integer>, String, TimeWindow> windowedStream = keyedStream.window(TumblingProcessingTimeWindows.of(Time.seconds(5)));
        //6.增量聚合计算
        //方式一：sum函数
//        SingleOutputStreamOperator<Tuple2<String, Integer>> result = windowedStream.sum(1);
        //方式二：累加器
//        SingleOutputStreamOperator<Tuple2<String, Integer>> result = windowedStream.aggregate(new MyAggFunc(), new MyWindowFunc());

        //方式三：全局窗口 [该函数作用与每个窗口]
     /*   SingleOutputStreamOperator<Tuple2<String, Integer>> result = windowedStream.apply(new WindowFunction<Tuple2<String, Integer>, Tuple2<String, Integer>, String, TimeWindow>() {
            @Override
            public void apply(String key, TimeWindow window, Iterable<Tuple2<String, Integer>> input, Collector<Tuple2<String, Integer>> out) throws Exception {
                //取出迭代器的长度
                ArrayList<Tuple2<String, Integer>> arrayList = Lists.newArrayList(input.iterator());
                //输出数据
                out.collect(new Tuple2<>(new Timestamp(window.getStart()) + ":" + key, arrayList.size()));
            }
        });
      */

        //方式四：底层接口实现全量窗口，用process可以获取context上下文，获取窗口相关信息
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = windowedStream.process(new ProcessWindowFunction<Tuple2<String, Integer>, Tuple2<String, Integer>, String, TimeWindow>() {
            @Override
            public void process(String key, Context context, Iterable<Tuple2<String, Integer>> elements, Collector<Tuple2<String, Integer>> out) throws Exception {
                //取出迭代器的长度
                ArrayList<Tuple2<String, Integer>> arrayList = Lists.newArrayList(elements.iterator());
                //输出数据
                out.collect(new Tuple2<>(new Timestamp(context.window().getStart()) + ":" + key, arrayList.size()));
            }
        });

        //7.打印
        result.print();

        //8.执行任务
        env.execute();
    }

    public static class MyAggFunc implements AggregateFunction<Tuple2<String, Integer>, Integer, Integer> {

        @Override
        public Integer createAccumulator() {  //初始化
            return 0;
        }

        @Override
        public Integer add(Tuple2<String, Integer> value, Integer accumulator) { //窗口内合并
            return accumulator + 1;
        }

        @Override
        public Integer getResult(Integer accumulator) {  //输出结果
            return accumulator;
        }

        @Override
        public Integer merge(Integer a, Integer b) {   //窗口间合并
            return a + b;
        }
    }

    public static class MyWindowFunc implements WindowFunction<Integer, Tuple2<String, Integer>, String, TimeWindow> {

        @Override
        public void apply(String key, TimeWindow window, Iterable<Integer> input, Collector<Tuple2<String, Integer>> out) throws Exception {
            //取出迭代器中的数据
            Integer next = input.iterator().next();
            //输出数据 [可以获取时间戳信息]
            out.collect(new Tuple2<>(new Timestamp(window.getStart()) + ":" + key, next));
        }
    }
}
