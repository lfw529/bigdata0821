package com.lfw.flink.state;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Iterator;

public class State_ListState_OP {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //2.读取端口数据并转换为JavaBean
        SingleOutputStreamOperator<WaterSensor> waterSensorDS = env.socketTextStream("hadoop105", 7777)
                .map(data -> {
                    String[] split = data.split(",");
                    return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
                });
        //3.统计元素个数
        waterSensorDS.map(new MyMapFunc()).print();
        //4.执行任务
        env.execute();
    }

    public static class MyMapFunc implements MapFunction<WaterSensor, Integer>, CheckpointedFunction {
        //定义状态
        private ListState<Integer> listState;
        private Integer count = 0;  //数据个数

        @Override
        public Integer map(WaterSensor value) throws Exception {
            count++;
            return count;
        }

        @Override  //Checkpoint 时会调用这个方法，我们要实现具体的snapshot逻辑，比如将哪些本地状态持久化
        public void snapshotState(FunctionSnapshotContext context) throws Exception {
            listState.clear();  //先清空再添加，否则会有很多数据
            listState.add(count);
        }

        @Override  //初始化状态:初始化时会调用这个方法，向本地状态中填充数据，每个子任务调用一次
        public void initializeState(FunctionInitializationContext context) throws Exception {
            listState = context.getOperatorStateStore().getListState(new ListStateDescriptor<Integer>("state", Integer.class));

            Iterator<Integer> iterator = listState.get().iterator();
            while (iterator.hasNext()) {
                count += iterator.next();
            }
        }
    }

}
