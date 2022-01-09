package com.lfw.flink.transform;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;

public class Transform_Reduce {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env1 = StreamExecutionEnvironment.getExecutionEnvironment();
        ArrayList<WaterSensor> waterSensors = new ArrayList<>();

        waterSensors.add(new WaterSensor("sensor_1", 1607527992000L, 20));
        waterSensors.add(new WaterSensor("sensor_1", 1607527994000L, 50));
        waterSensors.add(new WaterSensor("sensor_1", 1607527996000L, 50));
        waterSensors.add(new WaterSensor("sensor_2", 1607527993000L, 10));
        waterSensors.add(new WaterSensor("sensor_2", 1607527995000L, 30));

        KeyedStream<WaterSensor, String> kbStream = env1
                .fromCollection(waterSensors)
                .keyBy(WaterSensor::getId);

        kbStream.reduce(new ReduceFunction<WaterSensor>() {
            @Override
            public WaterSensor reduce(WaterSensor value1, WaterSensor value2) throws Exception {
                System.out.println("reducer function ...");
                return new WaterSensor(value1.getId(), value1.getTs(), value1.getVc() + value2.getVc());
            }
        }).print("reduce...");
        env1.execute();

        System.out.println("-------------------Lambda 表达式-----------------------");

        StreamExecutionEnvironment env2 = StreamExecutionEnvironment.getExecutionEnvironment();

        KeyedStream<WaterSensor, String> kbStream2 = env2
                .fromCollection(waterSensors)
                .keyBy(WaterSensor::getId);

        kbStream2.reduce(((value1, value2) -> {
            System.out.println("reducer function ...");
            return new WaterSensor(value1.getId(), value1.getTs(), value1.getVc() + value2.getVc());
        })).print("reduce...");
        env2.execute();
    }
}
