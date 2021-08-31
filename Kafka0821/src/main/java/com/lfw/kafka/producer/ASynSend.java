package com.lfw.kafka.producer;
import org.apache.kafka.clients.producer.*;
import java.util.Properties;

public class ASynSend {
    public static void main(String[] args) {
        //0.创建配置对象
        Properties props = new Properties();
        //kafka集群，broker-list
        props.put("bootstrap.servers", "hadoop102:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //1.床创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        //2.生产数据
        for (int i = 1; i <= 10; i++) {
            producer.send(new ProducerRecord<String, String>("Hello-Kafka", "hello$$" + i), new Callback() {
                /**
                 * 当成功发送消息后，或者发送过程中出现异常后,会调用该方法
                 * @param recordMetadata
                 * @param e
                 */
                @Override  //回调
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e != null) {
                        System.out.println("消息发送失败：" + e.getMessage());
                    } else {
                        System.out.println(recordMetadata.topic() + "----->" + recordMetadata.partition() + recordMetadata.offset());
                    }
                }
            });
        }
        //3.关闭生产者对象
        producer.close();
    }
}
