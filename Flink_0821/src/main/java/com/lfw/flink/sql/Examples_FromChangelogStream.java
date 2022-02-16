package com.lfw.flink.sql;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.types.Row;
import org.apache.flink.types.RowKind;
import org.junit.Test;

public class Examples_FromChangelogStream {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

    @Test
    public void example1() throws Exception {
        DataStream<Row> dataStream = env.fromElements(
                Row.ofKind(RowKind.INSERT, "Alice", 12),
                Row.ofKind(RowKind.INSERT, "Bob", 5),
                Row.ofKind(RowKind.UPDATE_BEFORE, "Alice", 12),
                Row.ofKind(RowKind.UPDATE_AFTER, "Alice", 100));

        //将数据流解释为一个表
        Table table = tableEnv.fromChangelogStream(dataStream);

        //在名称下注册表并执行聚合
        tableEnv.createTemporaryView("InputTable", table);
        tableEnv.executeSql("SELECT f0 AS name, SUM(f1) AS score FROM InputTable GROUP BY f0")
                .print();

        /*
        +----+--------------------------------+-------------+
        | op |                           name |       score |
        +----+--------------------------------+-------------+
        | +I |                            Bob |           5 |
        | +I |                          Alice |          12 |
        | -D |                          Alice |          12 |
        | +I |                          Alice |         100 |
        +----+--------------------------------+-------------+
         */
    }

    //将流解释为一个更新流
    //创建变更日志数据流
    @Test
    public void example2() throws Exception {
        DataStream<Row> dataStream = env.fromElements(
                Row.ofKind(RowKind.INSERT, "Alice", 12),
                Row.ofKind(RowKind.INSERT, "Bob", 5),
                Row.ofKind(RowKind.UPDATE_AFTER, "Alice", 100));

        //将数据流解释为一个表
        Table table = tableEnv.fromChangelogStream(
                dataStream,
                Schema.newBuilder().primaryKey("f0").build(),
                ChangelogMode.upsert());

        //在名称下注册表并执行聚合
        tableEnv.createTemporaryView("InputTable", table);
        tableEnv.executeSql("SELECT f0 AS name, SUM(f1) AS score FROM InputTable GROUP BY f0")
                .print();

        /*
        +----+--------------------------------+-------------+
        | op |                           name |       score |
        +----+--------------------------------+-------------+
        | +I |                            Bob |           5 |
        | +I |                          Alice |          12 |
        | -U |                          Alice |          12 |
        | +U |                          Alice |         100 |
        +----+--------------------------------+-------------+
         */
    }
}

