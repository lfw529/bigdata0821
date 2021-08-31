package com.lfw.kafka.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 消费者 -> 手动提交offset -> 同步提交
 */
public class KafkaCommit {
    public static void main(String[] args) {
        String topic = "Hello-Kafka";
        String group = "group3";
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
        consumer.subscribe(Collections.singleton(topic));
        try {
            //轮询消费
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(2));
                //迭代
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("消费到 record: " + record.topic() + " : " +
                            record.partition() + " : " + record.offset() + " : " +
                            record.key() + " : " + record.value());
                }
                // 提交offset

                // 同步提交  //---------------------------1
                //consumer.commitSync();  //提交由poll()返回的最新偏移量

                // 异步提交 //----------------------------2
//                consumer.commitAsync(new OffsetCommitCallback() {
//                    @Override
//                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
//                        if (exception != null) {
//                            System.out.println("提交offset失败.");
//                        } else {
//                            System.out.println("提交offset成功: " + offsets);
//                        }
//                    }
//                });

                //提交特定偏移量
                Map<TopicPartition,OffsetAndMetadata> offsets = new HashMap<>();

                ConsumerRecords<String, String> records1 = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record);
                    //记录每个主题的每个分区的偏移量
                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1, "no metaData");
                    /*TopicPartition 重写过 hashCode 和 equals 方法，所以能够保证同一主题和分区的实例不会被重复添加*/
                    offsets.put(topicPartition, offsetAndMetadata);
                }
                /*提交特定偏移量*/
                consumer.commitAsync(offsets, null);
            }
        } finally {
            consumer.close();
        }
    }
}
