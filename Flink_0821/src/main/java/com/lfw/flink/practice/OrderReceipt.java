package com.lfw.flink.practice;

import com.lfw.flink.bean.OrderEvent;
import com.lfw.flink.bean.TxEvent;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;
import java.util.HashMap;

public class OrderReceipt {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.读取2个文本数据创建流
        DataStreamSource<String> orderStreamDS = env.readTextFile("D:\\IdeaProjects\\bigdata0821\\Flink_0821\\src\\main\\resources\\OrderLog.csv");
        DataStreamSource<String> receiptStreamDS = env.readTextFile("D:\\IdeaProjects\\bigdata0821\\Flink_0821\\src\\main\\resources\\ReceiptLog.csv");
        //3.转换为JavaBean
        SingleOutputStreamOperator<OrderEvent> orderEventDS = orderStreamDS.flatMap(new FlatMapFunction<String, OrderEvent>() {
            @Override
            public void flatMap(String value, Collector<OrderEvent> out) throws Exception {
                String[] split = value.split(",");
                OrderEvent orderEvent = new OrderEvent(Long.parseLong(split[0]),
                        split[1],
                        split[2],
                        Long.parseLong(split[3]));
                if ("pay".equals(orderEvent.getEventType())) {
                    out.collect(orderEvent);
                }
            }
        });
        SingleOutputStreamOperator<TxEvent> txDS = receiptStreamDS.map(new MapFunction<String, TxEvent>() {
            @Override
            public TxEvent map(String value) throws Exception {
                String[] split = value.split(",");
                return new TxEvent(split[0], split[1], Long.parseLong(split[2]));
            }
        });
        //4.按照TXID进行分组
        KeyedStream<OrderEvent, String> orderEventStringKeyedStream = orderEventDS.keyBy(OrderEvent::getTxId);
        KeyedStream<TxEvent, String> txEventKeyedStream = txDS.keyBy(TxEvent::getTxId);
        //5.连接两个流
        ConnectedStreams<OrderEvent, TxEvent> connectedStreams = orderEventStringKeyedStream.connect(txEventKeyedStream);
        //6.处理两条流的数据
        SingleOutputStreamOperator<Tuple2<OrderEvent, TxEvent>> result = connectedStreams.process(new MyCoKeyedProcessFunc());
        //7.打印结果
        result.print();
        //8.执行任务
        env.execute();
    }

    public static class MyCoKeyedProcessFunc extends KeyedCoProcessFunction<String, OrderEvent, TxEvent, Tuple2<OrderEvent, TxEvent>> {
        private final HashMap<String, OrderEvent> orderEventHashMap = new HashMap<>();
        private final HashMap<String, TxEvent> txEventHashMap = new HashMap<>();

        @Override
        public void processElement1(OrderEvent value, Context ctx, Collector<Tuple2<OrderEvent, TxEvent>> out) throws Exception {
            if (txEventHashMap.containsKey(value.getTxId())) {
                TxEvent txEvent = txEventHashMap.get(value.getTxId());
                out.collect(new Tuple2<>(value, txEvent));
            } else {
                orderEventHashMap.put(value.getTxId(), value);
            }
        }

        @Override
        public void processElement2(TxEvent value, Context ctx, Collector<Tuple2<OrderEvent, TxEvent>> out) throws Exception {
            if (orderEventHashMap.containsKey(value.getTxId())) {
                OrderEvent orderEvent = orderEventHashMap.get(value.getTxId());
                out.collect(new Tuple2<>(orderEvent, value));
            } else {
                txEventHashMap.put(value.getTxId(), value);
            }
        }
    }
}
