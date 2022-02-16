package com.lfw.flink.sql;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.bridge.java.StreamStatementSet;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class AddTableAPIToDataStream {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        StreamStatementSet statementSet = tableEnv.createStatementSet();

        //create some source
        TableDescriptor sourceDescriptor = TableDescriptor.forConnector("datagen")
                .option("number-of-rows", "3")
                .schema(
                        Schema.newBuilder()
                                .column("myCol", DataTypes.INT())
                                .column("myOtherCol", DataTypes.BOOLEAN())
                                .build())
                .build();

        // create some sink
        TableDescriptor sinkDescriptor = TableDescriptor.forConnector("print").build();

        // add a pure Table API pipeline
        Table tableFromSource = tableEnv.from(sourceDescriptor);
        statementSet.addInsert(sinkDescriptor, tableFromSource);

        // use table sinks for the DataStream API pipeline
        DataStream<Integer> dataStream = env.fromElements(1, 2, 3);
        Table tableFromStream = tableEnv.fromDataStream(dataStream);
        statementSet.addInsert(sinkDescriptor, tableFromStream);

        // attach both pipelines to StreamExecutionEnvironment
        // (the statement set will be cleared after calling this method)
        statementSet.attachAsDataStream();

        // define other DataStream API parts
        env.fromElements(4, 5, 6).addSink(new DiscardingSink<>());

        // use DataStream API to submit the pipelines
        env.execute();
    }
}

/* 运行结果
    3> +I[641993098, false]
    1> +I[-1327063429, true]
    2> +I[381684374, true]
    +I[1]
    +I[2]
    +I[3]
 */
