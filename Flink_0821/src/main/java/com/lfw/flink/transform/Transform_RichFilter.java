package com.lfw.flink.transform;

import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Transform_RichFilter {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        //2.从文件读取数据
        DataStreamSource<String> stringDataStreamSource = env.readTextFile("D:\\IdeaProjects\\bigdata0821\\Flink_0821\\src\\main\\resources\\sensor.txt");

        //3.过滤数据，只取水位高于30的
        SingleOutputStreamOperator<String> result = stringDataStreamSource.filter(new MyRichFilterFunc());

        //4.打印数据
        result.print();

        //5.执行任务
        env.execute();
    }

    public static class MyRichFilterFunc extends RichFilterFunction<String> {

        @Override
        public void open(Configuration parameters) throws Exception {
            System.out.println("aaaa");
        }

        @Override
        public boolean filter(String value) throws Exception {
            String[] split = value.split(",");
            return Integer.parseInt(split[2]) > 30;
        }
    }
}
