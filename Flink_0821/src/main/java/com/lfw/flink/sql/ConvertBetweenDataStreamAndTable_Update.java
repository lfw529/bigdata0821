package com.lfw.flink.sql;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

public class ConvertBetweenDataStreamAndTable_Update {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //创建一个流
        DataStream<Row> dataStream = env.fromElements(
                Row.of("Alice", 12),
                Row.of("Bob", 10),
                Row.of("Alice", 100));

        //将仅插入的数据流解释为表
        Table inputTable = tableEnv.fromDataStream(dataStream).as("name", "score");

        //将表对象注册为视图并查询它, 该查询包含一个生成更新的聚合
        tableEnv.createTemporaryView("InputTable", inputTable);
        Table resultTable = tableEnv.sqlQuery(
                "SELECT name, SUM(score) FROM InputTable GROUP BY name");

        //将更新表解释为changelog数据流 [支持 Update], 用 toDataStream 会报错
        DataStream<Row> resultStream = tableEnv.toChangelogStream(resultTable);

        resultStream.print();
        env.execute();
    }
}
