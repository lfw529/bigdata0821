package com.lfw.kafka.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 生产者 -> 异步发送 -> 不带回调API
 * Kafka提供的配置类:
 * CommonClientConfigs : 通用的配置项
 * ProducerConfig ： 生产者的配置项
 * ConsumerConfig :  消费者的配置项
 */
public class SynSend {
    //main线程 ，如果没有topic,会自动创建
    public static void main(String[] args) {
        //0.创建配置对象
        Properties props = new Properties();
        //添加核心配置
        //kafka集群，broker-list
        //props.put("bootstrap.servers","hadoop102:9092");
        //使用配置类方式设置
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092");
        //指定 ack 级别，默认就是-1（all）
        //props.put("acks","all");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
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
        //1.创建生产者对象
        Producer<String, String> producer = new KafkaProducer<>(props);
        //2.生产数据
        for (int i = 1; i <= 10; i++) {
            try {
                ProducerRecord<String, String> record = new ProducerRecord<>("Hello-Kafka", "k" + i, "world" + i);
                /*同步发送消息*/
                RecordMetadata metadata = producer.send(record).get();
                System.out.printf("topic=%s, partition=%d, offset=%s \n",
                        metadata.topic(), metadata.partition(), metadata.offset());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        //3.关闭生产者对象
        producer.close();
    }
}
