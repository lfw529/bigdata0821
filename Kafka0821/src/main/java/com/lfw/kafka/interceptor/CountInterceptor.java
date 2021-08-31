package com.lfw.kafka.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class CountInterceptor implements ProducerInterceptor<String, String> {
    private Integer success = 0;
    private Integer fail = 0;

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            fail++;
        } else {
            success++;
        }
    }

    @Override
    public void close() {
        System.out.println("SUCCESS: " + success);
        System.out.println("FAIL:" + fail);
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
