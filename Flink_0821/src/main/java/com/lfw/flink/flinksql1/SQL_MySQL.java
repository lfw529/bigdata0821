package com.lfw.flink.flinksql1;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class SQL_MySQL {
    public static void main(String[] args) {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //2.注册SourceTable
        tableEnv.executeSql("create table source_sensor (id string, ts bigint, vc int) with("
                + "'connector' = 'kafka',"
                + "'topic' = 'topic_source',"
                + "'properties.bootstrap.servers' = 'hadoop105:9092,hadoop106:9092,hadoop107:9092',"
                + "'properties.group.id' = 'lfw',"
                + "'scan.startup.mode' = 'latest-offset',"
                + "'format' = 'csv'"
                + ")");

        //3.注册SinkTable:MySQL，表并不会自动创建
        tableEnv.executeSql("create table sink_sensor (id string, ts bigint, vc int) with("
                + "'connector' = 'jdbc',"
                + "'url' = 'jdbc:mysql://hadoop105:3306/test',"
                + "'table-name' = 'sink_table',"
                + "'username' = 'root',"
                + "'password' = '1234'"
                + ")");

        //4.执行查询Kafka数据
//        Table source_sensor = tableEnv.from("source_sensor");
        //5.将数据写入MySQL
//        source_sensor.executeInsert("sink_sensor");
        tableEnv.executeSql("insert into sink_sensor select * from source_sensor");  //直接一步书写
    }
}
