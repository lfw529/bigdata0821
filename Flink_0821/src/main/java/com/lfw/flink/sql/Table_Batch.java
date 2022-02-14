package com.lfw.flink.sql;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class Table_Batch {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);


        Table table = tableEnv.from(
                TableDescriptor.forConnector("datagen")  //datagen 是 Flink 内置数据生成器。
                        .option("number-of-rows", "10") // make the source bounded
                        .schema(
                                Schema.newBuilder()
                                        .column("uid", DataTypes.TINYINT())
                                        .column("payload", DataTypes.STRING())
                                        .build())
                        .build());

        // convert the Table to a DataStream and further transform the pipeline
        tableEnv.toDataStream(table)
                .keyBy(r -> r.<Byte>getFieldAs("uid"))
                .map(r -> "My custom operator: " + r.<String>getFieldAs("payload"))
                .executeAndCollect()
                .forEachRemaining(System.out::println);
    }
}
