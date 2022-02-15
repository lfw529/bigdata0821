package com.lfw.flink.sql;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

public class FromDataStream_DataTypes {
    //DataStream API还不支持不可变的POJO，
    //该类将生成一个泛型类型，默认情况下，它是表API中的原始类型
    public static class User {

        public final String name;

        public final Integer score;

        public User(String name, Integer score) {
            this.name = name;
            this.score = score;
        }
    }

    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

    //创建流
    DataStream<User> dataStream = env.fromElements(
            new User("Alice", 4),
            new User("Bob", 6),
            new User("Alice", 10));

    //由于无法访问原始类型的字段，因此每个流记录都被视为原子类型，指向具有单列'f0'的表`
    @Test
    public void example1() {
        Table table = tableEnv.fromDataStream(dataStream);
        table.printSchema();
    }

    //相反，使用表API的类型系统为列声明更有用的数据类型，在自定义模式中，并将以下 "as" 投影中的列重命名
    @Test
    public void example2() {
        Table table = tableEnv
                .fromDataStream(
                        dataStream,
                        Schema.newBuilder()
                                .column("f0", DataTypes.of(User.class))
                                .build())
                .as("user");
        table.printSchema();
        /*
            `user` *com.lfw.flink.sql.FromDataStream_DataTypes$User<`name` STRING, `score` INT>*
         */
    }

    //数据类型可以如上所述以反射方式提取或显式定义
    @Test
    public void example3() {
        Table table = tableEnv.fromDataStream(
                dataStream,
                Schema.newBuilder()
                        .column(
                                "f0",
                                DataTypes.STRUCTURED(
                                        User.class,
                                        DataTypes.FIELD("name", DataTypes.STRING()),
                                        DataTypes.FIELD("score", DataTypes.INT())
                                )
                        )
                        .build());
        table.printSchema();
        /*
         `f0` *com.lfw.flink.sql.FromDataStream_DataTypes$User<`name` STRING, `score` INT>*
         */
    }
}
