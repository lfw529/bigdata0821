package com.lfw.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;

public class CuratorConnection {
    public static void main(String[] args) {
        // session重连策略
/*
3秒后重连一次，只重连1次
RetryPolicy retryPolicy = new RetryOneTime(3000);
*/
/*
每3秒重连一次，重连3次
RetryPolicy retryPolicy = new RetryNTimes(3,3000);
*/
/*
每3秒重连一次，总等待时间超过10秒后停止重连
RetryPolicy retryPolicy=new RetryUntilElapsed(10000,3000);
*/
// baseSleepTimeMs * Math.max(1, random.nextInt(1 << (retryCount + 1)))
        // 创建连接对象
        CuratorFramework client = CuratorFrameworkFactory.builder()
                //IP地址端口号
                .connectString("192.168.1.102:2181,192.168.1.103:2181,192.168.1.104:2181")
                //会话超时时间
                .sessionTimeoutMs(5000)
                //重连机制
                .retryPolicy(new RetryOneTime(3000))
                //命名空间
                .namespace("create")
                //构建连接对象
                .build();
        //打开连接
        client.start();
        System.out.println(client.isStarted());
        //关闭连接
        client.close();
    }
}
