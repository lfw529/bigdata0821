package com.lfw.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SoloConsumer {
    public static void main(String[] args) {
        String topic = "Hello-Kafka";
        String group = "group6";
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
        props.put("enable.auto.commit", false);
        //创建一个消费者对象
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        List<TopicPartition> partitions = new ArrayList<>();
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);

        /*可以指定读取哪些分区 如这里假设只读取主题的 0 分区*/
        for (PartitionInfo partition : partitionInfos) {
            if (partition.partition() == 0) {
                partitions.add(new TopicPartition(partition.topic(), partition.partition()));
            }
        }
        //为消费者指定分区
        consumer.assign(partitions);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("partition = %s, key = %s, value = %s\n",
                        record.partition(), record.key(), record.value());
            }
            consumer.commitSync();
        }
    }
}
