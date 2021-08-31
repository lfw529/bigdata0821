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
 * 同步方式 getData(String path, boolean b, Stat stat)
 * 异步方式 getData(String path, boolean b，AsyncCallback.DataCallback callBack， Object ctx)
 * path                    - znode 路径。
 * b                         - 是否使用连接对象中注册的监视器。
 * stat                     - 返回 znode 的元数据。
 * callBack              - 异步回调接口
 * ctx                      - 传递上下文参数
 */
public class ZKGet {
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
        // arg1:节点的路径
        // arg2: 是否获取监听器数据
        // arg3:读取节点属性的对象
        Stat stat = new Stat();
        byte[] bys = zooKeeper.getData("/get/node1", false, stat);
        // 打印数据
        System.out.println(new String(bys));
        // 版本信息
        System.out.println(stat.getVersion());
    }

    @Test
    public void get2() throws Exception {
        //异步方式
        zooKeeper.getData("/get/node1", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                // 0 代表修改成功
                System.out.println(rc);
                //修改节点的路径
                System.out.println(path);
                //上下文的参数对象
                System.out.println(ctx);
                //数据
                System.out.println(new String(data));
                //属性对象
                System.out.println(stat.getVersion());
            }
        }, "I am Context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}
