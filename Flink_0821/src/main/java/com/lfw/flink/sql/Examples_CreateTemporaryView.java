package com.lfw.flink.sql;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

public class Examples_CreateTemporaryView {

    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
    //create some DataStream
    DataStream<Tuple2<Long, String>> dataStream = env.fromElements(
            Tuple2.of(12L, "Alice"),
            Tuple2.of(0L, "Bob"));

    //在当前会话中将数据流注册为视图"MyView"
    //所有列都是自动派生的
    @Test
    public void example1() {
        tableEnv.createTemporaryView("MyView", dataStream);
        tableEnv.from("MyView").printSchema();
    }

    //在当前会话中将数据流注册为视图"MyView"，提供一个 Schema 来调整类似于 fromDataStream 的列
    //在本例中，派生的非空信息已被删除
    @Test
    public void example2() {
        tableEnv.createTemporaryView(
                "MyView",
                dataStream,
                Schema.newBuilder()
                        .column("f0", "BIGINT")
                        .column("f1", "STRING")
                        .build());
        tableEnv.from("MyView").printSchema();
        /*
            `f0` BIGINT,
            `f1` STRING
         */
    }

    //如果只是关于重命名列，请在创建视图之前使用表API
    @Test
    public void example3() {
        tableEnv.createTemporaryView(
                "MyView",
                tableEnv.fromDataStream(dataStream).as("id", "name"));
        tableEnv.from("MyView").printSchema();
        /*
            `id` BIGINT NOT NULL,
            `name` STRING
         */
    }
}
