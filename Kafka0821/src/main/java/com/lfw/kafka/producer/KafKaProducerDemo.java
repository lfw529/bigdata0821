package com.lfw.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 自定义分区测试主类
 */
public class KafKaProducerDemo {
    //main线程
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //0.创建配置对象
        Properties props = new Properties();
        //kafka集群，broker-list
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092");
        //指定key 和 value的序列化
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //设置分区器
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.lfw.kafka.producer.MyPartitioner");
        //1. 创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        //2. 生产数据
        for (int i = 1; i <= 20; i++) {
            if (ThreadLocalRandom.current().nextInt() % 2 == 0) {
                ProducerRecord<String, String> record = new ProducerRecord<String, String>("Hello-Kafka", "kafka*_*" + i);
                try {
                    RecordMetadata meta = producer.send(record).get();
                    System.out.println(meta.topic() + " " + meta.partition() + " " + record.value());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            } else {
                ProducerRecord<String, String> record = new ProducerRecord<>("Hello-Kafka", "kfk*_*" + i);
                try {
                    RecordMetadata meta = producer.send(record).get();
                    System.out.println(meta.topic() + " " + meta.partition() + " " + record.value());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        //3. 关闭生产者对象
        producer.close();
    }
}


