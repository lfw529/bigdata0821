package com.lfw.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CuratorGetChild {
    String IP = "192.168.1.102:2181,192.168.1.103:2181,192.168.1.104:2181";
    CuratorFramework client;

    @Before
    public void before() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(IP).sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    @After
    public void after() {
        client.close();
    }

    @Test
    public void getChild1() throws Exception {
        //读取子节点数据
        List<String> list = client.getChildren()
                //节点路径
                .forPath("/get");
        for (String str : list) {
            System.out.println(str);
        }
    }

    @Test
    public void getchild2() throws Exception {
        //异步方式读取子节点数据
        client.getChildren().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                //节点路径
                System.out.println(event.getPath());
                //事件类型
                System.out.println(event.getType());
                //读取子节点数据
                List<String> list = event.getChildren();
                for (String str : list) {
                    System.out.println(str);
                }
            }
        }).forPath("/get");
        Thread.sleep(5000);
        System.out.println("结束");
    }
}
