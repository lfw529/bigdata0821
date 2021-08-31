package com.lfw.flink.flinksql3;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;


public class Function_UDTF {
    public static void main(String[] args) throws Exception {

        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //2.读取端口数据并转换为JavaBean
        SingleOutputStreamOperator<WaterSensor> waterSensorDS = env.socketTextStream("hadoop105", 7777)
                .map(line -> {
                    String[] split = line.split(",");
                    return new WaterSensor(split[0],
                            Long.parseLong(split[1]),
                            Integer.parseInt(split[2]));
                });

        //3.将流转换为动态表
        Table table = tableEnv.fromDataStream(waterSensorDS);

        //4.先注册再使用
        tableEnv.createTemporarySystemFunction("split", Spilt.class);

        //TableAPI
//        table
//                .joinLateral(call("split", $("id")))
//                .select($("id"), $("word"))
//                .execute()
//                .print();

        //SQL [拆分函数]
        tableEnv.sqlQuery("select id, word from "+ table + ",lateral table(split(id))").execute().print();

        //6.执行任务
        env.execute();
    }

    @FunctionHint(output = @DataTypeHint("ROW<word STRING>"))
    public static class Spilt extends TableFunction<Row> {

        public void eval(String value) {
            String[] split = value.split("_");
            for (String s : split) {
                collect(Row.of(s));
            }
        }
    }
}
