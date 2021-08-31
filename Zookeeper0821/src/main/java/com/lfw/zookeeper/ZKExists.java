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
 * 同步方法 exists(String path, boolean b)
 * 异步方法 exists(String path, boolean b，AsyncCallback.StatCallback callBack,Object ctx)
 * path                          - znode路径。
 * b                               - 是否使用连接对象中注册的监视器。
 * callBack                     - 异步回调接口。
 * ctx                            - 传递上下文参数
 */

public class ZKExists {
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
    public void exists1() throws Exception {
        // arg1:节点的路径
        Stat stat = zooKeeper.exists("/exists1", false);
        System.out.println(stat);   //null 为不存在
//        System.out.println(stat.getVersion());
    }

    @Test
    public void exists2() throws Exception {
        // 异步使用方式
        zooKeeper.exists("/exists1", false, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                // 0 判断成功
                System.out.println(rc);
                // 路径
                System.out.println(path);
                // 上下文参数
                System.out.println(ctx);
                // null 节点不存在
                System.out.println(stat.getVersion());
                //stat
                System.out.println(stat);
            }
        }, "I am Context");
        Thread.sleep(5000);
        System.out.println("结束");
    }
}
