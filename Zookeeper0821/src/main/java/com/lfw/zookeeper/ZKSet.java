package com.lfw.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * // 同步方式 setData(String path, byte[] data, int version)
 * // 异步方式 setData(String path, byte[] data, int version，AsyncCallback.StatCallback callBack， Object ctx)
 * path                        - znode 路径
 * data                        - 要存储在指定 znode 路径中的数据。
 * version                     - znode 的当前版本。每当数据更改时，ZooKeeper 会更新 znode 的版本号。
 * callBack                    - 异步回调接口
 * ctx                         - 传递上下文参数
 */

public class ZKSet {
    String IP = "192.168.6.102:2181";
    ZooKeeper zooKeeper;

    @Before
    public void before() throws Exception {
        // 计数器对象
        CountDownLatch countDownLatch = new CountDownLatch(1);
        // arg1:服务器的ip和端口
        // arg2:客户端与服务器之间的会话超时时间  以毫秒为单位的
        // arg3:监视器对象
        zooKeeper = new ZooKeeper(IP, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("连接创建成功!");
                    countDownLatch.countDown();
                }
            }
        });
        // 主线程阻塞等待连接对象的创建成功
        countDownLatch.await();
    }

    @After
    public void after() throws Exception {
        zooKeeper.close();
    }

    @Test
    public void set1() throws Exception {
        // arg1:节点的路径
        // arg2:节点修改的数据
        // arg3:版本号 -1代表版本号不作为修改条件
        Stat stat = zooKeeper.setData("/set/node1", "node13".getBytes(), 0);
        //节点的版本号
        System.out.println(stat.getVersion());
        //节点的创建时间
        System.out.println(stat.getCtime());
    }

    @Test
    public void set2() throws Exception {
        //异步方式修改节点
        zooKeeper.setData("/set/node2", "node21".getBytes(), -1, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                // 0 代表修改成功
                System.out.println(rc);
                //修改节点的路径
                System.out.println(path);
                //上下文的参数对象
                System.out.println(ctx);
                //节点的属性信息
                System.out.println(stat.getVersion());
            }
        }, "I am Context");
        Thread.sleep(50000);
        System.out.println("结束");
    }
}
