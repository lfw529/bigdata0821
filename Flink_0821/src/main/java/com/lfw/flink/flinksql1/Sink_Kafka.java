//package com.lfw.flink.flinksql1;
//
//import com.lfw.flink.bean.WaterSensor;
//import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.table.api.DataTypes;
//import org.apache.flink.table.api.Table;
//import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
//import org.apache.flink.table.descriptors.Csv;
//import org.apache.flink.table.descriptors.Kafka;
//import org.apache.flink.table.descriptors.Schema;
//import org.apache.kafka.clients.producer.ProducerConfig;
//
//import static org.apache.flink.table.api.Expressions.$;
//
//public class Sink_Kafka {
//    public static void main(String[] args) throws Exception {
//        //1.获取流执行环境
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);
//
//        //2.读取端口数据创建流并装换为JavaBean
//        SingleOutputStreamOperator<WaterSensor> waterSensorDS = env.socketTextStream("hadoop105", 7777)
//                .map(data -> {
//                    String[] split = data.split(",");
//                    return new WaterSensor(split[0],
//                            Long.parseLong(split[1]),
//                            Integer.parseInt(split[2]));
//                });
//
//        //3.创建表执行环境
//        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
//
//        //4.将流转换为动态表
//        Table sensorTable = tableEnv.fromDataStream(waterSensorDS);
//
//        //5.使用TableAPI过滤出"ws_001"的数据
//        Table selectTable = sensorTable
//                .where($("id").isEqual("ws_001"))
//                .select($("id"), $("ts"), $("vc"));
//
//        //6.将selectTable 写入 Kafka
//        tableEnv.connect(new Kafka()
//                .version("universal")
//                .topic("test")
//                .startFromLatest()
//                .sinkPartitionerRoundRobin()
//                .property(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop105:9092"))
//                .withSchema(new Schema()
//                        .field("id", DataTypes.STRING())
//                        .field("ts", DataTypes.BIGINT())
//                        .field("vc", DataTypes.INT()))
////                .withFormat(new Json())    //以Json格式输出，常用
//                .withFormat(new Csv())       //以Csv格式输出，但是有个bug,中间会空一行
//                .createTemporaryTable("sensor");
//        tableEnv.from("sensor");    //Source
//        selectTable.executeInsert("sensor");  //Sink
//
//        //7.执行任务
//        env.execute();
//    }
//}
