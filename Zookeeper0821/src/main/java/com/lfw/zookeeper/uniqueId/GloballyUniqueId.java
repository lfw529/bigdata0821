package com.lfw.zookeeper.uniqueId;

import com.lfw.zookeeper.watcher.ZKConnectionWatcher;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class GloballyUniqueId implements Watcher {

    //zk的连接串
    String IP = "192.168.6.102:2181";
    //计数器对象
    CountDownLatch countDownLatch = new CountDownLatch(1);
    //用户生成序列号节点
    String defaultPath = "/uniqueId";
    //连接对象
    ZooKeeper zooKeeper;

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            //捕获事件状态
            if (watchedEvent.getType() == Event.EventType.None) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("连接成功");
                    countDownLatch.countDown();
                } else if (watchedEvent.getState() == Event.KeeperState.Disconnected) {
                    System.out.println("断开连接！");
                } else if (watchedEvent.getState() == Event.KeeperState.Expired) {
                    System.out.println("会话超时！");
                    //重新连
                    zooKeeper = new ZooKeeper("192.168.6.102:2181", 5000, new ZKConnectionWatcher());
                } else if (watchedEvent.getState() == Event.KeeperState.AuthFailed) {
                    System.out.println("认证失败！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //构造方法
    public GloballyUniqueId() {
        try {
            //打开连接
            zooKeeper = new ZooKeeper(IP, 5000, this);
            //阻塞线程，等待连接的创建成功
            countDownLatch.await();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //生成id的方法
    public String getUniqueId() {
        String path = "";
        try {
            //创建临时有序节点
            path = zooKeeper.create(defaultPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        //   /uniqueId0000000001 取第9位之后的
        return path.substring(9);
    }

    public static void main(String[] args) {
        GloballyUniqueId globallyUniqueId = new GloballyUniqueId();
        for (int i = 1; i < 5; i++) {
            String id = globallyUniqueId.getUniqueId();
            System.out.println(id);
        }
    }
}
