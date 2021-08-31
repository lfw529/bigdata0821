package com.lfw.kafka.flume;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import java.util.List;
import java.util.Map;

public class HeaderInterceptor implements Interceptor {
    @Override
    public void initialize() {

    }

    /**
     * 判断event的body的内容:
     *    如果包含"atguigu",在event的header中添加 topic=first
     *    如果包含"shangguigu",在event的header中添加 topic=second
     */
    @Override
    public Event intercept(Event event) {
        //headers
        Map<String,String> headers = event.getHeaders();
        //body
        String body = new String(event.getBody());
        if (body.contains("atguigu")){
            headers.put("topic","first");
        }else if(body.contains("shangguigu")){
            headers.put("topic","second");
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
            return new HeaderInterceptor();
        }

        @Override
        public void configure(Context context) {
        }
    }
}
