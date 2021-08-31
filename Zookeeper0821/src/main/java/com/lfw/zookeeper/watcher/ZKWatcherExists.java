package com.lfw.zookeeper.watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * // 使用连接对象的监视器 exists(String path, boolean b)
 * // 自定义监视器 exists(String path, Watcher w)
 * // NodeCreated：节点创建
 * // NodeDeleted：节点删除
 * // NodeDataChanged：节点内容发生变化
 * path              - znode 路径。
 * b                   - 是否使用连接对象中注册的监视器。
 * w                  - 监视器对象。
 */
public class ZKWatcherExists {

    String IP = "192.168.6.102:2181";
    ZooKeeper zooKeeper = null;

    @Before
    public void before() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //连接zookeeper客户端
        zooKeeper = new ZooKeeper(IP, 6000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("连接对象的参数！");
                //连接成功
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                }
                System.out.println("path=" + watchedEvent.getPath());
                System.out.println("eventType=" + watchedEvent.getType());
            }
        });
        countDownLatch.await();
    }

    @After
    public void after() throws InterruptedException {
        zooKeeper.close();
    }

    @Test
    public void watchGetData1() throws KeeperException, InterruptedException {
        //arg1:节点的路径
        //arg2:使用连接对象中的watcher
        zooKeeper.getData("/watcher1", true, null);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    @Test
    public void watcherGetData2() throws KeeperException, InterruptedException {
        //arg1:节点的路径
        //arg2:使用连接对象中的watcher
        zooKeeper.getData("/watcher2", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("自定义watcher");
                System.out.println("path=" + watchedEvent.getPath());
                System.out.println("eventType=" + watchedEvent.getType());
            }
        }, null);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    @Test
    public void watcherExist3() throws KeeperException, InterruptedException {
        //watcher 一次性
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    System.out.println("自定义watcher");
                    System.out.println("path=" + watchedEvent.getPath());
                    System.out.println("eventType=" + watchedEvent.getType());
                    zooKeeper.exists("/watcher1", this);  //加上这句可以改变 watcher 一次性 ,变成持续获取
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        zooKeeper.exists("/watcher1", watcher);
        Thread.sleep(80000);
        System.out.println("结束");
    }

    @Test
    public void watcherExist4() throws KeeperException, InterruptedException {
        //注册多个监听器对象
        zooKeeper.exists("/watcher1", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("1");
                System.out.println("path=" + watchedEvent.getPath());
                System.out.println("eventType=" + watchedEvent.getType());
            }
        });
        zooKeeper.exists("/watcher1", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("2");
                System.out.println("path=" + watchedEvent.getPath());
                System.out.println("eventType=" + watchedEvent.getType());
            }
        });
        Thread.sleep(80000);
        System.out.println("结束");
    }
}
