package com.lfw.kafka.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class TimeInterceptor implements ProducerInterceptor<String,String> {
    /**
     * 拦截器的核心处理方法
     *
     * 需求: 在消息内容的前面添加 时间戳
     * @param record
     * @return
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        //获取消息的value
        String value = record.value();
        value = System.currentTimeMillis() + "->" +value;

        //重新封装 ProducerRecord 对象
        ProducerRecord<String,String> newRecord = new ProducerRecord<>(record.topic(),record.partition(),record.key(),value);
        return newRecord;
    }

    /**
     * 消息发送后,在ack前执行.
     * @param metadata  消息发送成功,会把消息的元数据封装到该对象中
     * @param exception 消息发送抛出异常.会把异常信息封装到该对象中
     */
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
