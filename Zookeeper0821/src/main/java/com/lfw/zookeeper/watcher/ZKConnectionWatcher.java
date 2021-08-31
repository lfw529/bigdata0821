package com.lfw.zookeeper.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKConnectionWatcher implements Watcher {
    //计算器对象
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    //连接对象
    static ZooKeeper zooKeeper;
    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            //事件类型
            if(watchedEvent.getType() == Event.EventType.None){
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected){
                    System.out.println("连接创建成功！");
                    countDownLatch.countDown();
                }else if(watchedEvent.getState() == Event.KeeperState.Disconnected){
                    System.out.println("断开连接！");
                }else if(watchedEvent.getState() == Event.KeeperState.Expired){
                    System.out.println("会话超时！");
                    //重新连
                    zooKeeper = new ZooKeeper("192.168.6.102:2181",5000,new ZKConnectionWatcher());
                }else if(watchedEvent.getState() == Event.KeeperState.AuthFailed){
                    System.out.println("认证失败！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try{
            zooKeeper = new ZooKeeper("192.168.6.102:2181",5000,new ZKConnectionWatcher());
            //阻塞线程等待连接的创建
            countDownLatch.await();
            //会话id
            System.out.println(zooKeeper.getSessionId());
            //添加授权用户（第四种方式）
            zooKeeper.addAuthInfo("digest","lfw:1234".getBytes());
            byte[] bs = zooKeeper.getData("/node1",false,null);
            System.out.println(new String(bs));
            Thread.sleep(50000);
            zooKeeper.close();
            System.out.println("结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
