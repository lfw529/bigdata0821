package com.lfw.flink.transform;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Transform_RichMap {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        //2.从文件读取数据
        DataStreamSource<String> stringDataStreamSource = env.readTextFile("D:\\IdeaProjects\\bigdata0821\\Flink_0821\\src\\main\\resources\\sensor.txt");

        //3.将每行数据转换为JavaBean
        SingleOutputStreamOperator<WaterSensor> waterSensorDS = stringDataStreamSource.map(new MyRichMapFunc());

        //4.打印结果数据
        waterSensorDS.print();

        env.execute();
    }

    //RichFunction富有的地方在于:1.声明周期方法,2.可以获取上下文执行环境,做状态编程
    public static class MyRichMapFunc extends RichMapFunction<String, WaterSensor> {
        @Override
        public void open(Configuration parameters) throws Exception {
            System.out.println("Open方法被调用！！");
        }

        @Override
        public WaterSensor map(String value) throws Exception {
            return null;
        }

        @Override
        public void close() throws Exception {
            System.out.println("Close方法被调用！！！");
        }
    }
}
