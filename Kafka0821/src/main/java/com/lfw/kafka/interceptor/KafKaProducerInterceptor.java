package com.lfw.kafka.interceptor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafKaProducerInterceptor {
    public static void main(String[] args) {
        //0. 创建配置对象
        Properties props  = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop102:9092");
        //指定ack级别,默认就是-1(all);
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        //producer重试次数
        props.put("retries", 5);
        //批次大小
        props.put("batch.size", 16384);
        //等待时间
        props.put("linger.ms", 1);
        //RecordAccumulator缓冲区大小
        props.put("buffer.memory", 33554432); // 32M
        //指定key 和 value的序列化
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //指定拦截器
        List<String> interceptors = new ArrayList<>();
        interceptors.add("com.lfw.kafka.interceptor.TimeInterceptor");
        interceptors.add("com.lfw.kafka.interceptor.CountInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,interceptors);
        //1. 创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        //2. 生产数据
        for (int i = 1; i <= 10 ; i++) {
            producer.send(new ProducerRecord<String,String>("atguigu","helloKafka>>>>>" + i));
        }
        //3. 关闭生产者对象
        producer.close();
    }
}
