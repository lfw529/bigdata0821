package com.lfw.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 生产者 -> ProducerRecord重载 --> 分区分配
 */
public class KafKaPartition {
    //main 线程
    public static void main(String[] args) throws InterruptedException {
        //0. 创建配置对象
        Properties props = new Properties();
        // 添加核心配置
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //1.创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        //2. 生产数据
        for (int i = 1; i <= 200; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("Hello-Kafka", "hello^_^" + i);
            //1. 没有partition,没有key ==>轮询/黏性
            try {
                RecordMetadata meta = producer.send(record).get();
                System.out.println(meta.topic() + " " + meta.partition() + " " + meta.offset());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("----------------------------");
        //2. 生产数据
        for (int i = 1; i <= 20; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("Hello-Kafka", "key" + i / 2, "hello@@" + i);
            //2. 没有partition,有key hash 取余
            try {
                RecordMetadata meta = producer.send(record).get();
                System.out.println(meta.topic() + " " + meta.partition() + " " + meta.offset());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("----------------------------");
        //2. 生产数据
        for (int i = 1; i <= 20; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("Hello-Kafka", 0, null, "hello%%" + i);
            //3. 有 partition,指定哪个分区被发送
            try {
                RecordMetadata meta = producer.send(record).get();
                System.out.println(meta.topic() + " " + meta.partition() + " " + meta.offset());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //3. 关闭生产者对象
        producer.close();  //如果不关闭，则生产的内容在缓冲区，来不及刷出，主线程就结束了（当数据量不超过16384，
        // 时间不超过1ms,数据存留在batch缓冲区，此时主线程已经结束）
        //TimeUnit.MILLISECONDS.sleep(10);
    }
}
