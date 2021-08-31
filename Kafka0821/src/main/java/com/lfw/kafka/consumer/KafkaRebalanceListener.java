package com.lfw.kafka.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class KafkaRebalanceListener {
    public static void main(String[] args) {
        String topic = "Hello-Kafka";
        String group = "group4";
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
        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
        consumer.subscribe(Collections.singleton(topic), new ConsumerRebalanceListener() {
            /*该方法会在消费者停止读取消息之后，再均衡开始之前就调用。如果在这里提交偏移量，下一个接管分区的消费者就知道该从哪里消费量*/
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.println("再均衡即将触发");
                //提交已经处理的偏移量
                consumer.commitSync(offsets);
            }

            /*该方法会在重新分配分区之后，消费者开始读取消息之前被调用*/
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            }
        });
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record);
                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1, "no metaData");
                    /*TopicPartition 重写过 hashCode 和 equals 方法，所以能够保证同一主题和分区的实例不会被重复添加*/
                    offsets.put(topicPartition, offsetAndMetadata);
                }
                consumer.commitAsync(offsets, null);
            }
        } finally {
            consumer.close();
        }
    }
}
