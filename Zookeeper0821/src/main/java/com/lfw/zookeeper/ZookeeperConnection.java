package com.lfw.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * ZooKeeper(String connectionString, int sessionTimeout, Watcher watcher)
 * connectionString                - zookeeper 主机
 * sessionTimeout                  - 会话超时（以毫秒为单位)
 * watcher                         - 实现“监视器”对象。zookeeper 集合通过监视器对象返回连接状态。
 */
public class ZookeeperConnection {
    public static void main(String[] args) {
        try {
            //计数器对象
            CountDownLatch countDownLatch = new CountDownLatch(1);
            // arg1:服务器的ip和端口
            // arg2:客户端与服务器之间的会话超时时间  以毫秒为单位的
            // arg3:监视器对象
            ZooKeeper zooKeeper = new ZooKeeper("192.168.1.102:2181", 5000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        System.out.println("连接创建成功！");
                        countDownLatch.countDown();
                    }
                }
            });
            //主线程阻塞等待连接对象的创建成功
            countDownLatch.await();
            System.out.println(zooKeeper.getSessionId());
            zooKeeper.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
