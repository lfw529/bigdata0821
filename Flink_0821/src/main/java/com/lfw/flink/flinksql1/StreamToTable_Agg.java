package com.lfw.flink.flinksql1;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

public class StreamToTable_Agg {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //2.读取端口数据创建流并装换为JavaBean
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

        //5.使用TableAPI 实现 select id,sum(vc) from sensor where vc>=20 group by id;
//        Table selectTable = sensorTable
//                .where($("vc").isGreaterOrEqual(20))
//                .groupBy($("id"))
//                .aggregate($("vc").sum().as("sum_vc"))
//                .select($("id"), $("sum_vc"));

        //老版本写法
        Table selectTable = sensorTable.groupBy("id")
                .select("id,id.count");

        //6.将selectTable转换为流进行输出
        // [此处不能用追加流，需要用撤回流来删除之前数据，比如：第一条数据20，如果第二条数据来了，也是20，追加之后就变成40，之前的数值也没有删除掉，换成撤回流之后，就会撤回第一条数据]
        DataStream<Tuple2<Boolean, Row>> rowDataStream = tableEnv.toRetractStream(selectTable, Row.class);
        rowDataStream.print();

        //7.执行任务
        env.execute();
    }
}
