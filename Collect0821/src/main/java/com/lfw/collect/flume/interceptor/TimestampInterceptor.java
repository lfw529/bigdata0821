package com.lfw.collect.flume.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.flume.source.kafka.KafkaSourceConstants;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 主要作用就是将一条用户行为数据中的ts设置到event的header中，来覆盖kafkaSource中设置的ts
 */
public class TimestampInterceptor implements Interceptor {
    @Override
    public void initialize() {
    }

    @Override
    public Event intercept(Event event) {
        String body = new String(event.getBody(), StandardCharsets.UTF_8);
        Map<String,String> headers = event.getHeaders();
        //解析json字段
        JSONObject jsonObject = JSON.parseObject(body);
        if(jsonObject.containsKey("ts")){
            headers.put(KafkaSourceConstants.TIMESTAMP_HEADER,jsonObject.getString("ts"));
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        for (Event event : events){
            intercept(event);
        }
        return events;
    }

    @Override
    public void close() {
    }

    public static class Builder implements Interceptor.Builder{

        @Override
        public Interceptor build() {
            return new TimestampInterceptor();
        }

        @Override
        public void configure(Context context) {
        }
    }
}
