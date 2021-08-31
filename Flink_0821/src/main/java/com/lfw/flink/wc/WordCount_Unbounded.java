package com.lfw.flink.wc;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class WordCount_Unbounded {
    public static void main(String[] args) throws Exception {
        //创建流处理执行环境 (区别于批处理)
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //用parameter tool工具从程序启动参数中提取配置项
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        int port = parameterTool.getInt("port");

        //从socket文本流读取数据
        DataStream<String> inputDataStream = env.socketTextStream(host, port);

        //基于数据流进行转换计算
        DataStream<Tuple2<String, Integer>> resultStream = inputDataStream.flatMap(new WordCount_Bounded.LineToTupleFlatMapFunc())
                .keyBy(0)
                .sum(1);

        resultStream.print();
        //启动任务
        env.execute();
    }
}
