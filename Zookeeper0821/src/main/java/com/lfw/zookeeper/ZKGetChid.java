package com.lfw.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 同步方式 getChildren(String path, boolean b)
 * 异步方式 getChildren(String path, boolean b,AsyncCallback.ChildrenCallback callBack,Object ctx)
 * path                  - Znode 路径。
 * b                       - 是否使用连接对象中注册的监视器。
 * callBack            - 异步回调接口。
 * ctx                    - 传递上下文参数
 */

public class ZKGetChid {
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
    public void get1() throws Exception {
        //arg1:节点的路径
        List<String> list = zooKeeper.getChildren("/get", false);
        for (String str : list) {
            System.out.println(str);
        }
    }

    @Test
    public void get2() throws Exception {
        //异步方法
        zooKeeper.getChildren("/get", false, new AsyncCallback.Children2Callback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
                // 0 代表修改成功
                System.out.println(rc);
                //修改节点的路径
                System.out.println(path);
                //上下文的参数对象
                System.out.println(ctx);
                //子节点信息
                for (String str : children) {
                    System.out.println(str);
                }
            }
        }, "I am Context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}
