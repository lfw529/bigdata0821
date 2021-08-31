package com.lfw.flink.flinksql2;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Session;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.lit;

public class TableAPI_GroupWindow_SessionWindow_ProcessTime {
    public static void main(String[] args) throws Exception {

        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //2.读取端口数据创建流并转换每一行数据为JavaBean对象
        SingleOutputStreamOperator<WaterSensor> waterSensorDS = env.socketTextStream("hadoop105", 7777)
                .map(line -> {
                    String[] split = line.split(",");
                    return new WaterSensor(split[0],
                            Long.parseLong(split[1]),
                            Integer.parseInt(split[2]));
                });

        //3.将流转换为表并指定处理时间
        Table table = tableEnv.fromDataStream(waterSensorDS,
                $("id"),
                $("ts"),
                $("vc"),
                $("pt").proctime());

        //4.开会话窗口计算WordCount
        Table result = table.window(Session.withGap(lit(5).second()).on($("pt")).as("sw"))
                .groupBy($("id"), $("sw"))
                .select($("id"), $("id").count());

        //5.将结果表转换为流进行输出 [注意：此处应该使用追加流，因为窗口只输出一次（行动算子出发），且不涉及修改数据，所以使用追加流，一般不用撤回流]
        tableEnv.toAppendStream(result, Row.class).print();

        //6.执行任务
        env.execute();
    }
}