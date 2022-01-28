package com.lfw.flink.state;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;


import java.util.ArrayList;
import java.util.List;

public class State_BufferingSink {

    public static void main(String[] args) {

    }

    public static class BufferingSink implements SinkFunction<Tuple2<String, Integer>>, CheckpointedFunction {
        private final int threshold;
        private transient ListState<Tuple2<String, Integer>> checkpointedState;
        private List<Tuple2<String, Integer>> bufferedElements;

        public BufferingSink(int threshold) {
            this.threshold = threshold;
            this.bufferedElements = new ArrayList<>();
        }

        @Override
        public void invoke(Tuple2<String, Integer> value, Context context) throws Exception {
            bufferedElements.add(value);
            if (bufferedElements.size() == threshold) {
                for (Tuple2<String, Integer> element : bufferedElements) {
                    //用户业务逻辑，写出数据到外部存储
                }
                bufferedElements.clear();
            }
        }

        //Operator 级别的 State 需要用户来实现快照保存逻辑
        @Override
        public void snapshotState(FunctionSnapshotContext context) throws Exception {
            checkpointedState.clear();
            for (Tuple2<String, Integer> element : bufferedElements) {
                checkpointedState.add(element);
            }
        }

        //Operator 级别的 State 需要用户来实现状态的初始化
        @Override
        public void initializeState(FunctionInitializationContext context) throws Exception {
            ListStateDescriptor<Tuple2<String, Integer>> descriptor =
                    new ListStateDescriptor<Tuple2<String, Integer>>(
                            "buffered-elements",
                            TypeInformation.of(new TypeHint<Tuple2<String, Integer>>() {
                                               }
                            ));
            checkpointedState = context.getOperatorStateStore().getListState(descriptor);
            if (context.isRestored()) {
                for (Tuple2<String, Integer> element : checkpointedState.get()) {
                    bufferedElements.add(element);
                }
            }
        }
    }
}
