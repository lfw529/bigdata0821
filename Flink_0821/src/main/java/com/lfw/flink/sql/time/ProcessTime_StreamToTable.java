package com.lfw.flink.sql.time;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import javax.xml.crypto.Data;

import static org.apache.flink.table.api.Expressions.$;

public class ProcessTime_StreamToTable {
    public static void main(String[] args) {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //2.读取文本数据创建流并转换为JavaBean对象
        SingleOutputStreamOperator<WaterSensor> waterSensorDS = env.readTextFile("Flink_0821/src/main/resources/sensor.txt")
                .map(line -> {
                    String[] split = line.split(",");
                    return new WaterSensor(split[0],
                            Long.parseLong(split[1]),
                            Integer.parseInt(split[2]));
                });
        //3.将流转换为表，并指定处理时间
//        Table table = tableEnv.fromDataStream(
//                waterSensorDS,
//                $("id"),
//                $("ts"),
//                $("vc"),
//                $("pt").proctime());

        Table table = tableEnv.fromDataStream(
                waterSensorDS,
                Schema.newBuilder()
                        .column("id", DataTypes.STRING())
                        .column("ts", DataTypes.BIGINT())
                        .column("vc", DataTypes.INT())    // 等价于：.columnByMetadata("vc1", DataTypes.INT()) 须字段名不一样
                        .columnByExpression("proc_time", "PROCTIME()")
                .build());

        //4.打印元数据信息
        table.printSchema();
    }
}
