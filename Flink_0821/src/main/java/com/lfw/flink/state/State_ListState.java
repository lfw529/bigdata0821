package com.lfw.flink.state;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

public class State_ListState {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //2.读取端口数据并转换为JavaBean
        SingleOutputStreamOperator<WaterSensor> waterSensorDS = env.socketTextStream("hadoop102", 7777)
                .map(data -> {
                    String[] split = data.split(",");
                    return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
                });

        //3.按照传感器id分组
        KeyedStream<WaterSensor, String> keyedStream = waterSensorDS.keyBy(WaterSensor::getId);

        //4.使用ListState实现每隔传感器最高的三个水位线
        keyedStream.map(new RichMapFunction<WaterSensor, List<WaterSensor>>() {
            //定义状态
            private ListState<WaterSensor> top3State;

            @Override
            public void open(Configuration parameters) throws Exception {
                top3State = getRuntimeContext().getListState(new ListStateDescriptor<WaterSensor>("list-state", WaterSensor.class));
            }

            @Override
            public List<WaterSensor> map(WaterSensor value) throws Exception {
                //将当前数据加入状态
                top3State.add(value);

                //取出状态中的数据（水位高度）并排序
                List<WaterSensor> vcs = new ArrayList<>();
                for (WaterSensor vc : top3State.get()) {
                    vcs.add(vc);
                }
                //降序排序
                vcs.sort((o1, o2) -> o2.getVc() - o1.getVc());

                //判断当前数据是否超过3条，如果超过，则删除最后一条
                if (vcs.size() > 3) {
                    vcs.remove(3);
                }
                //更新状态
                top3State.update(vcs);
                //返回状态
                return vcs;
            }
        }).print();
        //5.执行任务
        env.execute();
    }
}
