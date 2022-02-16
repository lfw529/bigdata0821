package com.lfw.flink.sql;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.data.StringData;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.time.Instant;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.row;

public class Examples_ToChangelogStream {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

    @Test
    public void example1() throws Exception {
        // create Table with event-time
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

        // 以最简单、最通用的方式转换为数据流（无事件时间）
        Table simpleTable = tableEnv
                .fromValues(row("Alice", 12), row("Alice", 2), row("Bob", 12))
                .as("name", "score")
                .groupBy($("name"))
                .select($("name"), $("score").sum());

        tableEnv.toChangelogStream(simpleTable)
                .executeAndCollect()
                .forEachRemaining(System.out::println);
        /*
            +I[Bob, 12]
            +I[Alice, 12]
            -U[Alice, 12]
            +U[Alice, 14]
         */
    }

    @Test
    public void example2() throws Exception {
        //以最简单、最通用的方式转换为数据流（使用事件时间）
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
        DataStream<Row> dataStream = tableEnv.toChangelogStream(table);

        //由于'event_time'是模式中的单个时间属性，因此将其设置为流记录的时间戳；然而，与此同时，它仍然保留 Row
        dataStream.process(
                new ProcessFunction<Row, Void>() {
                    @Override
                    public void processElement(Row row, Context ctx, Collector<Void> out) throws Exception {
                        //prints: [name,score,event_time]
                        System.out.println(row.getFieldNames(true));

                        //timestamp exists twice
                        assert ctx.timestamp() == row.<Instant>getFieldAs("event_time").toEpochMilli();
                    }
                });
        env.execute();
    }

    //转换为DataStream，但将time属性写为元数据列，这意味着它不再是物理模式的一部分
    @Test
    public void example3() throws Exception {
        //以最简单、最通用的方式转换为数据流（使用事件时间）
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

        DataStream<Row> dataStream = tableEnv.toChangelogStream(
                table,
                Schema.newBuilder()
                        .column("name", "STRING")
                        .column("score", "INT")
                        .columnByMetadata("rowtime", "TIMESTAMP_LTZ(3)")
                        .build());

        //流记录的时间戳由元数据定义；这不是 Row 的一部分
        dataStream.process(
                new ProcessFunction<Row, Void>() {
                    @Override
                    public void processElement(Row row, Context ctx, Collector<Void> out) throws Exception {
                        //prints:[name, score]
                        System.out.println(row.getFieldNames(true));

                        //timestamp exists once
                        System.out.println(ctx.timestamp());
                    }
                });
        env.execute();
    }

    //对于高级用户，也可以使用更多的内部数据结构来提高效率
    //请注意，这里提到这一点只是为了完整性，因为它使用内部数据结构增加了复杂性和额外的类型处理
    //但是，将 TIMESTAMP_LTZ 列转换为 "Long" 或将 STRING 转换为 "byte[]" 可能比较方便，如果需要，结构化类型也可以表示为"行"
    @Test
    public void example4() throws Exception {
        //以最简单、最通用的方式转换为数据流（使用事件时间）
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

        DataStream<Row> dataStream = tableEnv.toChangelogStream(
                table,
                Schema.newBuilder()
                        .column(
                                "name",
                                DataTypes.STRING().bridgedTo(StringData.class))
                        .column(
                                "score",
                                DataTypes.INT())
                        .column(
                                "event_time",
                                DataTypes.TIMESTAMP_LTZ(3).bridgedTo(Long.class))
                        .build());
        dataStream.print();
        env.execute();
    }
}
