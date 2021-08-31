package com.lfw.flink.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class WordCount_Bounded {
    public static void main(String[] args) throws Exception {
        //创建流处理执行环境 (区别于批处理)
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setParallelism(1);
        //env.disableOperatorChaining();

        //读取文件创建流  //有状态
        DataStreamSource<String> inputDataStream = env.readTextFile("D:\\IdeaProjects\\bigdata0821\\Flink_0821\\src\\main\\resources\\input.txt");
        //基于数据流进行转换计算
        DataStream<Tuple2<String, Integer>> resultStream = inputDataStream.flatMap(new LineToTupleFlatMapFunc())
                .keyBy(0)
                .sum(1);

        resultStream.print("111>");
        //7.启动任务
        env.execute();
    }

    public static class LineToTupleFlatMapFunc implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            String[] words = value.split(" ");
            for (String word : words) {
                out.collect(new Tuple2<>(word, 1));
            }
        }
    }
}
