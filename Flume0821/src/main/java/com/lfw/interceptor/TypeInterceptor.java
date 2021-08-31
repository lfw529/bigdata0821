package com.lfw.interceptor;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class TypeInterceptor implements Interceptor {
    //声明一个存放事件的集合
    private List<Event> addHeaderEvents;
    @Override
    public void initialize() {
        //初始化存放事件的集合
        addHeaderEvents = new ArrayList<>();
    }

    //单个事件拦截
    @Override
    public Event intercept(Event event) {
        //1.获取事件中的头信息
        Map<String, String> headers = event.getHeaders();
        //2.获取事件中的body信息
        String body = new String(event.getBody());
        //3.根据body中是否有"atguigu"来决定添加怎样的头信息
        if (body.contains("atguigu")) {
            //4.添加头信息
            headers.put("type", "first");
        } else {
            //4.添加头信息
            headers.put("type", "second");
        }
        return event;
    }
    //批量事件拦截(在批量中调用单个事件)
    @Override
    public List<Event> intercept(List<Event> events) {
        //1.清空集合
        addHeaderEvents.clear();
        //2.遍历 events
        for (Event event : events) {
            //3.给每一个事件添加头信息
            addHeaderEvents.add(intercept(event));
        }
        return addHeaderEvents;
    }

    @Override
    public void close() {
    }

    public static class Builder implements Interceptor.Builder {
        @Override
        public Interceptor build() {
            return new TypeInterceptor();
        }

        @Override
        public void configure(Context context) {
        }
    }
}
