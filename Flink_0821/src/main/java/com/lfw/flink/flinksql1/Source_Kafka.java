//package com.lfw.flink.flinksql1;
//
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.table.api.DataTypes;
//import org.apache.flink.table.api.Table;
//import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
//import org.apache.flink.table.descriptors.Json;
//import org.apache.flink.table.descriptors.Kafka;
//import org.apache.flink.table.descriptors.Schema;
//import org.apache.flink.types.Row;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//
//import static org.apache.flink.table.api.Expressions.$;
//
//public class Source_Kafka {
//    public static void main(String[] args) throws Exception {
//        //1.获取执行环境
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);
//        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
//
//        //2.使用连接器的方式读取Kafka的数据
//        tableEnv.connect(new Kafka()
//                .version("universal")
//                .topic("test")
//                .startFromLatest()
//                .property(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop105:9092")
//                .property(ConsumerConfig.GROUP_ID_CONFIG, "BigData0821"))
//                .withSchema(new Schema()
//                        .field("id", DataTypes.STRING())
//                        .field("ts", DataTypes.BIGINT())
//                        .field("vc", DataTypes.INT()))
//                .withFormat(new Json())    //支持Json格式：{ "id":"ws_001", "ts":1577844001, "vc":45 }
////               .withFormat(new Csv())    //支持csv格式：ws_001,1577844001,45
//                .createTemporaryTable("sensor");
//
//        //3.使用连接器创建表
//        Table sensor = tableEnv.from("sensor");
//
//        //4.查询数据
//        Table resultTable = sensor.groupBy($("id"))
//                .select($("id"), $("id").count());
//
//        //5.将表转换为流进行输出
//        tableEnv.toRetractStream(resultTable, Row.class).print();
//
//        //7.执行任务
//        env.execute();
//    }
//}
