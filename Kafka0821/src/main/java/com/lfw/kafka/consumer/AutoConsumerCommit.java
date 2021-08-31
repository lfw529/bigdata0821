package com.lfw.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Properties;

/**
 * 消费者 -> 自动提交offset --> offset重置问题
 * 目前的现象: 当我们启动一个新的消费者组，进行消费，默认是消费不到之前的数据.
 * Default: latest
 */
public class AutoConsumerCommit {
    public static void main(String[] args) {
        String topic = "Hello-Kafka";
        String group = "group2";
        //0.  创建配置对象
        Properties props = new Properties();
        // 指定kafka集群
        props.put("bootstrap.servers", "hadoop102:9092");
        // 指定消费者组
        props.put("group.id", group);
        // 指定key 和value的反序列化
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // 指定offset重置
        props.put("auto.offset.reset", "earliest");
        //配置自动提交
        props.put("enable.auto.commit", true);
        //1. 创建一个消费者对象
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        //载入订阅列表
        consumer.subscribe(Collections.singletonList(topic));
        //3.轮询消费
        try {
            while (true) {
                //轮询获取数据
                ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                //迭代
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("消费到 record: " + record.topic() + " : " +
                            record.partition() + " : " + record.offset() + " : " +
                            record.key() + " : " + record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }
}
