package com.lfw.flink.sql;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
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
    public void example1() {
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
    }

}
