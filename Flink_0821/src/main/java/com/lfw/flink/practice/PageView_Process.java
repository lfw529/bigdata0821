package com.lfw.flink.practice;

import com.lfw.flink.bean.UserBehavior;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class PageView_Process {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.读取文本数据
        DataStreamSource<String> readTextFile = env.readTextFile("D:\\IdeaProjects\\bigdata0821\\Flink_0821\\src\\main\\resources\\UserBehavior.csv");
        //3.转换为JavaBean并过滤出PV数据
        SingleOutputStreamOperator<UserBehavior> userBehaviorDS = readTextFile.flatMap(new FlatMapFunction<String, UserBehavior>() {
            @Override
            public void flatMap(String value, Collector<UserBehavior> out) throws Exception {
                //a.按照","分割
                String[] split = value.split(",");
                //b.封装JavaBean对象
                UserBehavior userBehavior = new UserBehavior(Long.parseLong(split[0]),
                        Long.parseLong(split[1]),
                        Integer.parseInt(split[2]),
                        split[3],
                        Long.parseLong(split[4]));
                //c.选择要输出的数据
                if ("pv".equals(userBehavior.getBehavior())) {
                    out.collect(userBehavior);
                }
            }
        });
        //userBehaviorDS.print();
        //4.指定Key分组
        KeyedStream<UserBehavior, String> keyedStream = userBehaviorDS.keyBy(data -> "PV");
        //5.计算总和
        SingleOutputStreamOperator<Integer> result = keyedStream.process(new KeyedProcessFunction<String, UserBehavior, Integer>() {
            Integer count = 0;
            @Override
            public void processElement(UserBehavior value, Context ctx, Collector<Integer> out) throws Exception {
                count++;
                out.collect(count);
            }
        });
        //6.打印输出
        result.print();
        //7.执行任务
        env.execute();
    }
}
