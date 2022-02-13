package com.lfw.flink.sql;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

public class ConvertBetweenDataStreamAndTable {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //创建一个流
        DataStream<String> dataStream = env.fromElements("Alice", "Bob", "John");

        //将插入的数据流解释为表
        Table inputTable = tableEnv.fromDataStream(dataStream);

        //注册一个表对象作为视图来查询
        tableEnv.createTemporaryView("InputTable", inputTable);
        Table resultTable = tableEnv.sqlQuery("SELECT UPPER(f0) FROM InputTable");

        //再次将insert only表解释为数据流
        DataStream<Row> resultStream = tableEnv.toDataStream(resultTable);

        resultStream.print();
        env.execute();
    }
}
