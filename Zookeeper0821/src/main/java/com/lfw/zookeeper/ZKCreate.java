package com.lfw.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 同步方式 create(String path, byte[] data, List<ACL> acl, CreateMode createMode)
 * 异步方式 create(String path, byte[] data, List<ACL> acl, CreateMode createMode， AsyncCallback.StringCallback callBack,Object ctx)
 * path               - znode 路径。例如，/node1 /node1/node11
 * data               - 要存储在指定 znode 路径中的数据
 * acl                  - 要创建的节点的访问控制列表。zookeeper API 提供了一个静态接口
 * ZooDefs.Ids    - 来获取一些基本的 acl 列表。例如，ZooDefs.Ids.OPEN_ACL_UNSAFE 返回打开 znode 的 acl 列表。
 * createMode   - 节点的类型，这是一个枚举。
 * callBack           - 异步回调接口
 * ctx                   - 传递上下文参数
 */
public class ZKCreate {
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
    public void create1() throws Exception {
        // arg1:节点的路径
        // arg2:节点的数据
        // arg3:权限列表  world:anyone:cdrwa
        // arg4:节点类型  持久化节点
        zooKeeper.create("/create/node1", "node1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void create2() throws Exception {
        // Ids.READ_ACL_UNSAFE world:anyone:r
        zooKeeper.create("/create/node2", "node2".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void create3() throws Exception {
        //world 授权模式
        //权限列表
        List<ACL> acls = new ArrayList<ACL>();
        //授权模式和授权对象
        Id id = new Id("world", "anyone");
        //权限位置
        acls.add(new ACL(ZooDefs.Perms.READ, id));
        acls.add(new ACL(ZooDefs.Perms.WRITE, id));
        zooKeeper.create("/create/node3", "node3".getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create4() throws Exception {
        //ip 授权模式
        //权限列表
        List<ACL> acls = new ArrayList<>();
        //授权模式和授权对象
        Id id = new Id("ip", "192.168.1.102");
        //权限设置
        acls.add(new ACL(ZooDefs.Perms.ALL, id));
        zooKeeper.create("/create/node4", "node4".getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create5() throws Exception {
        //auth 授权模式
        //添加授权模式
        zooKeeper.addAuthInfo("digest", "lfw:1234".getBytes());
        zooKeeper.create("/create/node5", "node5".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
    }

    @Test
    public void create6() throws Exception {
        //auth 授权模式
        //添加授权用户
        zooKeeper.addAuthInfo("digest", "lfw:1234".getBytes());
        //权限列表
        List<ACL> acls = new ArrayList<ACL>();
        //授权模式和授权对象
        Id id = new Id("auth", "lfw");
        //权限设置
        acls.add(new ACL(ZooDefs.Perms.READ, id));
        zooKeeper.create("/create/node6", "node6".getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create7() throws Exception {
        //digest 授权模式
        //权限列表
        List<ACL> acls = new ArrayList<ACL>();
        //授权模式和授权对象
        Id id = new Id("digest", "lfw:PjoxjnnTGAKTEJ584DRwjjT5Xdg=");
        // 权限设置
        acls.add(new ACL(ZooDefs.Perms.ALL, id));
        zooKeeper.create("/create/node7", "node7".getBytes(), acls, CreateMode.PERSISTENT);
    }

    @Test
    public void create8() throws Exception {
        // 持久化顺序节点
        // Ids.OPEN_ACL_UNSAFE world:anyone:cdrwa
        String result = zooKeeper.create("/create/node8", "node8".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println(result);
    }

    @Test
    public void create9() throws Exception {
        //  临时节点
        // Ids.OPEN_ACL_UNSAFE world:anyone:cdrwa
        String result = zooKeeper.create("/create/node9", "node9".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(result);
    }

    @Test
    public void create10() throws Exception {
        // 临时顺序节点
        // Ids.OPEN_ACL_UNSAFE world:anyone:cdrwa
        String result = zooKeeper.create("/create/node10", "node10".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(result);
    }

    @Test
    public void create11() throws Exception {
        // 异步方式创建节点 （回调函数：会返回一些信息来告诉客户端创建成功）
        zooKeeper.create("/create/node11", "node11".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, String name) {
                // 0 代表创建成功
                System.out.println(rc);
                // 节点的路径
                System.out.println(path);
                // 上下文参数
                System.out.println(ctx);
            }
        }, "I am context");
        Thread.sleep(10000);
        System.out.println("结束");
    }
}

