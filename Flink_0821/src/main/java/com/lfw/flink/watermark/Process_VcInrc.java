package com.lfw.flink.watermark;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.runtime.messages.checkpoint.AbstractCheckpointMessage;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class Process_VcInrc {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(8);
        //2.读取端口数据并转换为JavaBean
        SingleOutputStreamOperator<WaterSensor> waterSensorDS = env.socketTextStream("hadoop102", 7777)
                .map(data -> {
                    String[] split = data.split(",");
                    return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
                });

        //3.按照传感器 ID 分组
        KeyedStream<WaterSensor, String> keyedStream = waterSensorDS.keyBy(WaterSensor::getId);
        //4.使用ProcessFunction实现连续时间内水位不下降，则报警，且将报警信息输出到侧输出流
        SingleOutputStreamOperator<WaterSensor> result = keyedStream.process(new KeyedProcessFunction<String, WaterSensor, WaterSensor>() {
            private Integer lastVc = Integer.MIN_VALUE;
            private Long timerTs = Long.MIN_VALUE;

            @Override
            public void processElement(WaterSensor value, Context ctx, Collector<WaterSensor> out) throws Exception {
                System.out.println(lastVc);
                //取出水位线
                Integer vc = value.getVc();
                //将当前水位线与上一次进行比较
                if (vc > lastVc && timerTs == Long.MIN_VALUE) {
                    //注册定时器
                    long ts = ctx.timerService().currentProcessingTime() + 10000L;  //10s 定时器
                    System.out.println("注册定时器：" + ts);
                    ctx.timerService().registerProcessingTimeTimer(ts);  //物理时间 10s

                    //更新上一次水位线值，定时器的时间戳
                    timerTs = ts;
                } else if (vc < lastVc) {
                    //删除定时器
                    ctx.timerService().deleteProcessingTimeTimer(timerTs);
                    System.out.println("删除定时器：" + timerTs);
                    timerTs = Long.MIN_VALUE;
                }

                lastVc = vc;

                //输出数据
                out.collect(value);
            }

            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<WaterSensor> out) throws Exception {
                ctx.output(new OutputTag<String>("sideOut") {
                           },
                        ctx.getCurrentKey() + "连续10s水位线没有下降！");
                timerTs = Long.MIN_VALUE;
            }
        });
        //5.打印实际
        result.print("主流");
        result.getSideOutput(new OutputTag<String>("sideOut") {
        }).print("SideOutput");
        //6.执行任务
        env.execute();
    }
}
