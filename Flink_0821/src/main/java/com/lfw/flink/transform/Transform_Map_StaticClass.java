package com.lfw.flink.transform;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Transform_Map_StaticClass {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.fromElements(1, 2, 3, 4, 5)
                .map(new MyMapFunction()).print();
        env.execute();
    }

    public static class MyMapFunction implements MapFunction<Integer, Integer> {

        @Override
        public Integer map(Integer value) throws Exception {
            return value * value;
        }
    }
}
