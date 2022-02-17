package com.lfw.flink.sql;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.junit.Test;

import java.time.Instant;

public class Examples_ToDataStream {
    //具有可变列的POJO，由于未定义完全赋值构造函数，字段命令按字母顺序排列 [event_time,name,score]
    public static class User {
        public String name;
        public Integer score;
        public Instant event_time;
    }

    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

    @Test
    public void example1() throws Exception {
        tableEnv.executeSql(
                "CREATE TABLE GeneratedTable "
                        + "("
                        + "  name STRING,"
                        + "  score INT,"
                        + "  event_time TIMESTAMP_LTZ(3),"
                        + "  WATERMARK FOR event_time AS event_time - INTERVAL '10' SECOND"
                        + ")"
                        + "WITH ('connector'='datagen')");

        Table table = tableEnv.from("GeneratedTable");

        //示例1：使用对行实例的默认转换, 由于'event_time'是一个单独的 rowtime 属性，因此它被插入到数据流中,元数据和水印被传播
        DataStream<Row> dataStream = tableEnv.toDataStream(table);
        dataStream.print();
        env.execute();
    }

    @Test
    public void example2() throws Exception {
        tableEnv.executeSql(
                "CREATE TABLE GeneratedTable "
                        + "("
                        + "  name STRING,"
                        + "  score INT,"
                        + "  event_time TIMESTAMP_LTZ(3),"
                        + "  WATERMARK FOR event_time AS event_time - INTERVAL '10' SECOND"
                        + ")"
                        + "WITH ('connector'='datagen')");

        Table table = tableEnv.from("GeneratedTable");

        //==示例2==
        //从类 "User" 中提取数据类型，planner 会对字段重新排序，并在可能的情况下插入隐式强制转换，以转换内部类型，
        //将数据结构转换为所需的结构化类型
        //由于'event_time'是一个单独的rowtime属性，因此它被插入到数据流中，元数据和水印被传播
        DataStream<User> dataStream = tableEnv.toDataStream(table, User.class);
        dataStream.print();
        env.execute();
    }

    @Test
    public void example3() throws Exception {
        tableEnv.executeSql(
                "CREATE TABLE GeneratedTable "
                        + "("
                        + "  name STRING,"
                        + "  score INT,"
                        + "  event_time TIMESTAMP_LTZ(3),"
                        + "  WATERMARK FOR event_time AS event_time - INTERVAL '10' SECOND"
                        + ")"
                        + "WITH ('connector'='datagen')");

        Table table = tableEnv.from("GeneratedTable");

        //数据类型可以如上所述以反射方式提取或显式定义
        DataStream<User> dataStream =
                tableEnv.toDataStream(
                        table,
                        DataTypes.STRUCTURED(
                                User.class,
                                DataTypes.FIELD("name", DataTypes.STRING()),
                                DataTypes.FIELD("score", DataTypes.INT()),
                                DataTypes.FIELD("event_time", DataTypes.TIMESTAMP_LTZ(3))));
        dataStream.print();
        env.execute();
    }
}
