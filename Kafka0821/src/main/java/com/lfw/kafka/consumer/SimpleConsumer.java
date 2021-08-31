package com.lfw.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Properties;

/**
 * 消费者 -> 自动提交offset
 */
public class SimpleConsumer {
    public static void main(String[] args) {
        String topic = "Hello-Kafka";
        String group = "group1";
        //0.创建配置对象
        Properties props = new Properties();
        //指定kafka集群
        props.put("bootstrap.servers", "hadoop102:9092");
        //指定消费者组
        props.put("group.id", group);
        //指定 key 和 value 的反序列化
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //1. 创建一个消费者对象
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        //2.订阅主题
//        List<String> topics = new ArrayList<>();
//        topics.add("first");
//        topics.add("second");
//        topics.add("atguigu");
//        //topics.add("xxxxx");  //没有也可以注册
        consumer.subscribe(Collections.singletonList(topic));  //返回仅包含指定对象的不可变列表。返回的列表是可序列化的。
        //3. 轮询消费
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
/**  程序相关解释
 * while (true)：这是一个无限循环。消费者实际上是一个长期运行的应用程序，它通过持续轮询向 Kafka 请求数据。
 * consumer.poll(Duration.of(100, ChronoUnit.MILLIS))：这一行代码非常重要。消费者必须持续对Kafka进行轮询，否则会被认为已经死亡。
 * 它的分区会被认为已经死亡，它的分区会被移交给群组里的其他消费者。传给poll()方法的参数是一个超时时间，它会在指定的毫秒数内一直等待broker返回数据。
 * poll()：方法返回一个记录列表。每条记录都包含了记录所有主题的信息，记录所在分区的信息、记录所在分区的偏移量，以及键值对。一般遍历列表，逐条处理。
 */