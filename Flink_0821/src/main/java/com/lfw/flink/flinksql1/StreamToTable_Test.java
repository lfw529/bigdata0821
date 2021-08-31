package com.lfw.flink.flinksql1;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

public class StreamToTable_Test {
    public static void main(String[] args) throws Exception {
        //1.获取流执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //2.读取端口数据创建流并转换为JavaBean
        SingleOutputStreamOperator<WaterSensor> waterSensorDS = env.socketTextStream("hadoop105", 7777)
                .map(data -> {
                    String[] split = data.split(",");
                    return new WaterSensor(split[0],
                            Long.parseLong(split[1]),
                            Integer.parseInt(split[2]));
                });

        //3.创建表执行环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //4.将流转换为动态表
        Table sensorTable = tableEnv.fromDataStream(waterSensorDS);

        //5.使用TableAPI过滤出"ws_001"的数据
//        Table selectTable = sensorTable  //新版本写法
//                .where($("id").isEqual("ws_001"))
//                .select($("id"), $("ts"), $("vc"));

        //老版本写法
        Table selectTable = sensorTable
                .where("id ='ws_001'")
                .select("id,ts,vc");

        //6.将selectTable转换为流进行输出
        DataStream<Row> rowDataStream = tableEnv.toAppendStream(selectTable, Row.class);

//        DataStream<Tuple2<Boolean, Row>> rowDataStream = tableEnv.toRetractStream(selectTable, Row.class);  //撤回流，包括 delete 和 insert
        rowDataStream.print();

        //7.执行任务
        env.execute();
    }
}
