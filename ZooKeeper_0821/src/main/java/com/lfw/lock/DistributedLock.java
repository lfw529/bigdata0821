package com.lfw.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOError;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DistributedLock {
    //zookeeper server 列表
    private final String connectString = "hadoop102:2181,hadoop103:2181,hadoop104:2181";
    //超时时间
    private final int sessionTimeout = 2000;

    private ZooKeeper zk;

    private String rootNode = "locks";
    private String subNode = "seq-";
    //当前 client 等待的子节点
    private String waitPath;

    //Zookeeper 连接
    private final CountDownLatch connectLatch = new CountDownLatch(1);

    //Zookeeper 节点等待
    private final CountDownLatch waitLatch = new CountDownLatch(1);

    //当前client创建的子节点
    private String currentNode;

    //和 zk 服务器建立连接，并创建根节点
    public DistributedLock() throws IOException, InterruptedException, KeeperException {
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                //连接建立时，打开latch，唤醒wait在该latch上的线程
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    connectLatch.countDown();
                }
                //发生了waitPath的删除事件
                if (event.getType() == Event.EventType.NodeDeleted && event.getPath().equals(waitPath)) {
                    waitLatch.countDown();
                }
            }
        });
        //等待连接建立
        connectLatch.await();

        //获取根节点状态
        Stat stat = zk.exists("/" + rootNode, false);

        //如果根节点不存在，则创建根节点，根节点类型为永久节点
        if (stat == null) {
            System.out.println("根节点不存在");
            zk.create("/" + rootNode, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    //加锁方法
    public void zkLock() {
        try {
            //在根节点下创建临时顺序节点，返回值为创建节点路径
            currentNode = zk.create("/" + rootNode + "/" + subNode, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            //wait 一小会，让结果更清晰一些
            Thread.sleep(10);
            //注意，没有必要监听"/locks"的子节点的变化情况
            List<String> childrenNodes = zk.getChildren("/" + rootNode, false);
            //列表中只有一个子节点，那肯定解释currentNode，说明client获得锁
            if (childrenNodes.size() == 1) {
            } else {
                //对根节点下的所有临时顺序节点进行从小到大排序
                Collections.sort(childrenNodes);
                //当前节点名称
                String thisNode = currentNode.substring(("/" + rootNode + "/").length());
                //获取当前节点的位置
                int index = childrenNodes.indexOf(thisNode);

                if (index == -1) {
                    System.out.println("数据异常");
                } else if (index == 0) {
                    // index == 0，说明thisNode在列表中最小，当前client获得锁
                } else {
                    //获得排名比currentNode前1位的节点
                    this.waitPath = "/" + rootNode + "/" + childrenNodes.get(index - 1);
                    //在waitPath上注册监听器，当waitPath被删除时，zookeeper会回调监听器的process方法
                    zk.getData(waitPath, true, new Stat());
                    //进入等待锁状态
                    waitLatch.await();
                }
            }
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    //解锁方法
    public void zkUnlock() {
        try {
            zk.delete(this.currentNode, -1);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }
}
