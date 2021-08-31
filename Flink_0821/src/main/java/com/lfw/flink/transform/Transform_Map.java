package com.lfw.flink.transform;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Transform_Map {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //从文件读取数据
        DataStreamSource<String> stringDataStreamSource = env.readTextFile("D:\\IdeaProjects\\bigdata0821\\Flink_0821\\src\\main\\resources\\sensor.txt");

        //3.转换为JavaBean并打印数据
        stringDataStreamSource.map(new MapFunction<String, WaterSensor>() {
            @Override
            public WaterSensor map(String value) throws Exception {
                String[] split = value.split(",");
                return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
            }
        }).print();

        env.execute();
    }
}

