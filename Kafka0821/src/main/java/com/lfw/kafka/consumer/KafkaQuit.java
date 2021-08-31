package com.lfw.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class KafkaQuit {
    public static void main(String[] args) {
        String topic = "Hello-Kafka";
        String group = "group5";
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
        consumer.subscribe(Collections.singleton(topic));

        /*调用 wakeup 优雅的退出*/
        final Thread mainThread = Thread.currentThread();
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (sc.hasNext()) {
                if ("exit".equals(sc.next())) {
                    consumer.wakeup();
                    try {
                        /*等待主线程完成提交偏移量、关闭消费者等操作*/
                        mainThread.join();
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                for (ConsumerRecord<String, String> rd : records) {
                    System.out.printf("topic = %s,partition = %d, key = %s, value = %s, offset = %d,\n",
                            rd.topic(), rd.partition(), rd.key(), rd.value(), rd.offset());
                }
            }
        } catch (WakeupException e) {
            //对于 wakeup() 调用引起的 WakeupException 异常可以不必处理
        } finally {
            consumer.close();
            System.out.println("consumer 关闭");
        }
    }
}
