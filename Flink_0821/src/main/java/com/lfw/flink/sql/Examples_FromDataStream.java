package com.lfw.flink.sql;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

import java.time.Instant;

public class Examples_FromDataStream {

    //some example POJOs
    public static class User {
        public String name;

        public Integer score;

        public Instant event_time;

        public User() {
        }

        public User(String name, Integer score, Instant event_time) {
            this.name = name;
            this.score = score;
            this.event_time = event_time;
        }
    }

    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

    DataStream<User> dataStream =
            env.fromElements(
                    new User("Alice", 4, Instant.ofEpochMilli(1000)),
                    new User("Bob", 6, Instant.ofEpochMilli(1001)),
                    new User("Alice", 10, Instant.ofEpochMilli(1002)));

    // 案例1：自动派生所有物理列
    @Test
    public void example1() {
        Table table = tableEnv.fromDataStream(dataStream);
        table.printSchema();
    }

    // 案例2：自动删除所有物理列, 但是添加计算列（在本例中用于创建proctime属性列）
    @Test
    public void example2() {
        Table table = tableEnv.fromDataStream(
                dataStream,
                Schema.newBuilder()
                        .columnByExpression("proc_time", "PROCTIME()")
                        .build());
        table.printSchema();
    }

    // 案例3：自动派生所有物理列, 但是添加计算列（在本例中用于创建行时属性列）, 以及自定义水印策略
    @Test
    public void example3() {
        Table table =
                tableEnv.fromDataStream(
                        dataStream,
                        Schema.newBuilder()
                                .columnByExpression("rowtime", "CAST(event_time AS TIMESTAMP_LTZ(3))")
                                .watermark("rowtime", "rowtime - INTERVAL '10' SECOND")
                                .build());
        table.printSchema();
    }

    // 案例4：自动派生所有物理列,但是访问流记录的时间戳来创建rowtime属性列,还依赖于DataStream API中生成的水印,我们假设之前已经为"数据流"定义了水印策略（不是本例的一部分）
    @Test
    public void example4() {
        Table table =
                tableEnv.fromDataStream(
                        dataStream,
                        Schema.newBuilder()
                                .columnByMetadata("rowtime", "TIMESTAMP_LTZ(3)")
                                .watermark("rowtime", "SOURCE_WATERMARK()")
                                .build());
        table.printSchema();
    }

    // 案例5：手动定义物理列, 在这个例子中,
    //  -我们可以将时间戳的默认精度从9降低到3
    //  -我们也会投射专栏，并在开头加上"事件时间"
    @Test
    public void example5() {
        Table table = tableEnv.fromDataStream(
                dataStream,
                Schema.newBuilder()
                        .column("event_time", "TIMESTAMP_LTZ(3)")
                        .column("name", "STRING")
                        .column("score", "INT")
                        .watermark("event_time", "SOURCE_WATERMARK()")
                        .build());
        table.printSchema();
    }
}

