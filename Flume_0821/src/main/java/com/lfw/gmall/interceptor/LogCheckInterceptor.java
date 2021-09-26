package com.lfw.gmall.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

//ETL 检查器
public class LogCheckInterceptor implements Interceptor {

    @Override
    public void initialize() {
    }

    @Override
    public Event intercept(Event event) {
        //获取 event 的 body
        String log = new String(event.getBody(), StandardCharsets.UTF_8);
        //检验Log数据是否完整
        //boolean valid = JSON.isValid(log); //检查不了内部结构
        try {
            JSON.parse(log);
        } catch (JSONException e) {
            return null;
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        //将不能使用的log过滤掉，用迭代器操作，因为集合本身的方法不支持remove操作
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (intercept(event) == null) {
                iterator.remove();
            }
        }
        return events;
    }

    @Override
    public void close() {
    }

    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new LogCheckInterceptor();
        }

        @Override
        public void configure(Context context) {
        }
    }
}
