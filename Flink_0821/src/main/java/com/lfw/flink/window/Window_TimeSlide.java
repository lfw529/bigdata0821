package com.lfw.flink.window;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class Window_TimeSlide {
    public static void main(String[] args) throws Exception {

        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //2.读取端口数据
        DataStreamSource<String> socketTextStream = env.socketTextStream("hadoop105", 7777);

        //3.压平并转换为数组
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

        //5.开窗滑动窗口,6,2
        WindowedStream<Tuple2<String, Integer>, String, TimeWindow> windowedStream = keyedStream.window(SlidingProcessingTimeWindows.of(Time.seconds(6),Time.seconds(2)));

        //6.聚合计算
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = windowedStream.sum(1);

        //7.打印
        result.print();

        //8.执行任务
        env.execute();
    }
}
