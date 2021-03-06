package com.lfw.flink.transform;

import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

public class Transform_Process {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //2.读取端口数据
        DataStreamSource<String> socketTextStream = env.socketTextStream("hadoop105", 9999);

        //3.使用Process实现压平功能
        SingleOutputStreamOperator<String> wordDS = socketTextStream.process(new ProcessFlatMapFunc());

        //4.使用Process实现Map功能
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordToOneDS = wordDS.process(new ProcessMapFunc());

        //5.按照单词分组
        KeyedStream<Tuple2<String, Integer>, String> keyedStream = wordToOneDS.keyBy(data -> data.f0);

        //6.计算总和
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = keyedStream.sum(1);

        //7.打印
        result.print();

        //8.执行任务
        env.execute();
    }

    public static class ProcessFlatMapFunc extends ProcessFunction<String, String> {
        //生命周期方法
        @Override
        public void open(Configuration parameters) throws Exception {
        }

        @Override
        public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
            //运行时上下文，状态编程
            RuntimeContext runtimeContext = getRuntimeContext();

            String[] words = value.split(" ");
            for (String word : words) {
                out.collect(word);
            }

            //定时器
            TimerService timerService = ctx.timerService();
            timerService.registerProcessingTimeTimer(1245L);

            //获取当前处理数据的时间
            timerService.currentProcessingTime();
            timerService.currentWatermark();//事件时间

            //侧输出流
            //ctx.output();
        }
    }

    public static class ProcessMapFunc extends ProcessFunction<String, Tuple2<String, Integer>> {
        @Override
        public void processElement(String value, Context ctx, Collector<Tuple2<String, Integer>> out) throws Exception {
            out.collect(new Tuple2<>(value, 1));
        }
    }
}
