package com.lfw.flink.flinksql3;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.ScalarFunction;

public class Function_UDF {
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

        //4.不注册函数直接使用 [缺点：不能复用]
//        table.select(call(MyLength.class, $("id"))).execute().print();

        //5.先注册再使用
        tableEnv.createTemporarySystemFunction("mylen", MyLength.class);

        //TableAPI
//        table.select($("id"), call("mylen", $("id"))).execute().print();

        //SQL
        tableEnv.sqlQuery("select id,mylen(id) from " + table).execute().print();

        //6.执行任务
        env.execute();

    }

    public static class MyLength extends ScalarFunction {

        public int eval(String value) {
            return value.length();
        }
    }
}
