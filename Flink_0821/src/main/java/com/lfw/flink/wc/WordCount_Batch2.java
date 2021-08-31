package com.lfw.flink.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.*;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class WordCount_Batch2 {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSource<String> input = env.readTextFile("D:\\IdeaProjects\\bigdata0821\\Flink_0821\\src\\main\\resources\\input.txt");

        FlatMapOperator<String, String> wordDS = input.flatMap(new MyFlatFunc());

        MapOperator<String, Tuple2<String, Integer>> wordToOneDS = wordDS.map((MapFunction<String, Tuple2<String, Integer>>) value -> {
            return new Tuple2<>(value, 1);
            ////return Tuple2.of(value,1);
        }).returns(Types.TUPLE(Types.STRING, Types.INT));

        UnsortedGrouping<Tuple2<String, Integer>> groupBy = wordToOneDS.groupBy(0);

        AggregateOperator<Tuple2<String, Integer>> result = groupBy.sum(1);
        result.print();
    }

    public static class MyFlatFunc implements FlatMapFunction<String, String> {

        @Override
        public void flatMap(String value, Collector<String> out) throws Exception {
            String[] words = value.split(" ");
            for (String word : words) {
                out.collect(word);
            }
        }
    }
}