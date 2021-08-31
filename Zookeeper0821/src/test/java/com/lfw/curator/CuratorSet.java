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

public class CuratorSet {
    String IP = "192.168.1.102:2181,192.168.1.103:2181,192.168.1.104:2181";
    CuratorFramework client;

    @Before
    public void before() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(IP).sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace("set")
                .build();
        client.start();
    }

    @After
    public void after() {
        client.close();
    }

    @Test
    public void set1() throws Exception{
        //更新节点
        client.setData()
                //arg1:节点的路径
                //arg2:节点的数据
                .forPath("/node1","node11".getBytes());
        System.out.println("结束");
    }

    @Test
    public void set2() throws Exception{
        client.setData()
                //默认版本号
                .withVersion(-1)
                .forPath("/node11","node111111".getBytes());
        System.out.println("结束");
    }

    @Test
    public void set3() throws Exception{
        //异步方式修改节点数据
        client.setData()
                .withVersion(-1).inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                //节点的路径
                System.out.println(event.getPath());
                //事件的类型
                System.out.println(event.getType());
            }
        }).forPath("/node1","node1".getBytes());
        Thread.sleep(5000);
        System.out.println("结束");
    }
}
