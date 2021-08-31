package com.lfw.hdfs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 客户端性质的开发.
 * 1. 创建客户端对象
 * 2. 调用相应的方法实现相应的功能
 * 3. 关闭客户端对象
 */
public class HdfsClientDemo {

    /**
     * 文件和文件夹判断
     */
    @Test
    public void testListStatus() throws IOException, URISyntaxException, InterruptedException {
        //1.获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"), configuration, "lfw");
        //2.判断是文件还是文件夹
        FileStatus[] listStatus = fs.listStatus(new Path("/"));
        for (FileStatus fileStatus : listStatus) {
            //如果是文件
            if (fileStatus.isFile()) {
                System.out.println("f:" + fileStatus.getPath().getName());
            } else {
                System.out.println("d:" + fileStatus.getPath().getName());
            }
        }
        //3.关闭资源
        fs.close();
    }

    /**
     * 文件详情查看
     */
    @Test
    public void testListFiles() throws IOException, URISyntaxException, InterruptedException {
        //1.获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"), configuration, "lfw");
        //2.获取文件详情
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
        while (listFiles.hasNext()) {
            LocatedFileStatus status = listFiles.next();
            //输出详情
            //文件名称
            System.out.println(status.getPath().getName());
            //长度
            System.out.println(status.getLen());
            //权限
            System.out.println(status.getPermission());
            //分组
            System.out.println(status.getGroup());
            //获取存储的块信息
            BlockLocation[] blockLocations = status.getBlockLocations();
            for (BlockLocation blockLocation : blockLocations) {
                //获取块存取主机节点
                String[] hosts = blockLocation.getHosts();
                for (String host : hosts) {
                    System.out.println(host);
                }
            }
            System.out.println("------------------分割线--------------------");
        }
        //3.关闭资源
        fs.close();
    }

    /**
     * 文件夹删除
     */
    @Test
    public void testDelete() throws IOException, URISyntaxException, InterruptedException {
        //1.获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"), configuration, "lfw");
        //2.执行删除
        //如果要删除的路径是一个文件，参数recursive 是true还是false都行
        //如果要删除的路径是一个目录，参数recursive 开启true就是递归删除，如果填false，而下级目录还有文件，就会报错
        fs.delete(new Path("/banzhang1.txt"), true);
        //3.关闭资源
        fs.close();
    }

    /**
     * 文件名更改/移动
     */
    @Test
    public void testRename() throws IOException, URISyntaxException, InterruptedException {
        //1.获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"), configuration, "lfw");
        //2.修改文件名称
        fs.rename(new Path("/banzhang.txt"), new Path("/banhua.txt"));
        //3.关闭资源
        fs.close();
    }

    /**
     * 文件下载
     */
    @Test
    public void testCopyToLocalFile() throws IOException, URISyntaxException, InterruptedException {
        //1.获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"), configuration, "lfw");
        //2.执行下载操作
        //boolean delSrc 指是否将原文件删除
        //Path src 指要下载的文件路径
        //Path dst 指将文件下载到路径
        //boolean useRawLocalFileSystem 是否开启文件校验
        fs.copyToLocalFile(false, new Path("/banzhang.txt"), new Path("e:/banhua.txt"), true);
        //3.关闭资源
        fs.close();
    }

    /**
     * 文件上传
     */
    @Test
    public void testCopyFromLocalFile() throws IOException, InterruptedException, URISyntaxException {
        //1.获取文件系统
        Configuration configuration = new Configuration();
        //设置副本数为2个
        configuration.set("dfs.replication", "2");
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"), configuration, "lfw");
        //2.上传文件
        fs.copyFromLocalFile(new Path("e:/banzhang.txt"), new Path("/banzhang1.txt"));
        //3.关闭资源
        fs.close();
    }

    /**
     * 获取客户端对象
     */
    @Test
    public void testHdfsClient() throws URISyntaxException, IOException, InterruptedException {
        /**
         * 三个参数：
         * 1.URI : 指定hdfs的位置，实际上就是namenode的地址
         *
         * 2.Configuration : 用于添加配置
         *
         * 3.User : 指定操作hdfs的用户
         */
        //URI uri = new URI("hdfs://hadoop102:8020"); //或者
        URI uri = URI.create("hdfs://hadoop102:8020");

        Configuration conf = new Configuration();
        String user = "lfw";
        FileSystem fs = FileSystem.get(uri, conf, user);
        System.out.println(fs);
        System.out.println(fs.getClass());//org.apache.hadoop.hdfs.DistributedFileSystem
        fs.close();
    }

}
