package com.lfw.zookeeper.center;

import com.lfw.zookeeper.watcher.ZKConnectionWatcher;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class MyConfigCenter implements Watcher {

    //zk的连接串
    String IP = "192.168.6.102:2181";
    //计数器对象
    CountDownLatch countDownLatch = new CountDownLatch(1);
    //连接对象
    static ZooKeeper zooKeeper;
    //用于本地化存储配置信息
    private String url;
    private String username;
    private String password;

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
                //当配置信息发生变化时
            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                initValue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //构造方法
    public MyConfigCenter() {
        initValue();
    }

    //连接zookeeper服务器，读取配置信息
    public void initValue() {
        try {
            //创建连接对象
            zooKeeper = new ZooKeeper(IP, 5000, this);
            //阻塞线程，等待连接的创建成功
            countDownLatch.await();
            //读取配置信息
            this.url = new String(zooKeeper.getData("/config/url", true, null));
            this.username = new String(zooKeeper.getData("/config/username", true, null));
            this.password = new String(zooKeeper.getData("/config/password", true, null));
        } catch (IOException | InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            for (int i = 1; i < 20; i++) {
                MyConfigCenter myConfigCenter = new MyConfigCenter();
                System.out.println("url:" + myConfigCenter.getUrl());
                System.out.println("username:" + myConfigCenter.getUsername());
                System.out.println("password:" + myConfigCenter.getPassword());
                System.out.println("########################################");
                Thread.sleep(5000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
