package com.lfw.flink.transform;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Transform_Union {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Integer> stream1 = env.fromElements(1, 2, 3, 4, 5);
        DataStreamSource<Integer> stream2 = env.fromElements(10, 20, 30, 40, 50);
        DataStreamSource<Integer> stream3 = env.fromElements(100, 200, 300, 400, 500);

        // 把多个流union在一起成为一个流, 这些流中存储的数据类型必须一样: 水乳交融
        stream1.union(stream2).union(stream3).print();

        env.execute();
    }
}

