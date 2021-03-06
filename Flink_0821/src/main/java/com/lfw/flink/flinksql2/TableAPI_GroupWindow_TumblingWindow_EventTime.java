package com.lfw.flink.flinksql2;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.Tumble;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.lit;

public class TableAPI_GroupWindow_TumblingWindow_EventTime {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //2.读取文本数据转换为JavaBean并提取时间戳生成WaterMark
        WatermarkStrategy<WaterSensor> waterSensorWatermarkStrategy = WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                .withTimestampAssigner(new SerializableTimestampAssigner<WaterSensor>() {
                    @Override
                    public long extractTimestamp(WaterSensor element, long recordTimestamp) {
                        return element.getTs() * 1000L;
                    }
                });
        SingleOutputStreamOperator<WaterSensor> waterSensorDS = env.socketTextStream("hadoop105", 7777)
                .map(line -> {
                    String[] split = line.split(",");
                    return new WaterSensor(split[0],
                            Long.parseLong(split[1]),
                            Integer.parseInt(split[2]));
                }).assignTimestampsAndWatermarks(waterSensorWatermarkStrategy);

        //3.将流转换为表并指定事件事件字段
        Table table = tableEnv.fromDataStream(waterSensorDS,
                $("id"),
                $("ts"),
                $("vc"),
                $("rt").rowtime());

        //4.基于事件时间的滚动窗口
        Table result = table.window(Tumble.over(lit(5).seconds()).on($("rt")).as("tw"))
                .groupBy($("id"), $("tw"))
                .select($("id"), $("id").count());

        //5.转换为流进行打印
        tableEnv.toAppendStream(result, Row.class).print();

        //6.执行任务
        env.execute();
    }
}
