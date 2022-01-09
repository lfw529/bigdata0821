package com.lfw.flink.transform;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Transform_Aggregation {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Integer> stream = env.fromElements(1, 2, 3, 4, 5);
        //按照奇数和偶数分组，2号流为偶数，10号流为奇数
        KeyedStream<Integer, String> kbStream = stream.keyBy(ele -> ele % 2 == 0 ? "奇数" : "偶数");
        //分组求和---> 2号流：[2, 2+4=6], 10号流：[1, 1+3=4, 1+3+5=9]
        kbStream.sum(0).print("sum");  //2 6 1 4 9
        //分组求最大值---> 2号流：[2, 4], 10号流：[1, 3, 5]
        kbStream.max(0).print("max");  // 2 4 1 3 5
        //分组求最小值---> 2号流：[2, 2], 10号流：[1, 1, 1]
        kbStream.min(0).print("min");  // 2 2 1 1 1
        env.execute();
    }
}
