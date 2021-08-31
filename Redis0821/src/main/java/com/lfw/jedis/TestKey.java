package com.lfw.jedis;

import redis.clients.jedis.Jedis;

import java.util.Set;

public class TestKey {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.1.102", 6379);

        System.out.println("清空数据：" + jedis.flushDB());
        System.out.println("判断某个键是否存在：" + jedis.exists("username"));
        System.out.println("新增<username1,lfw1>的键值对：" + jedis.set("username1", "lfw1"));
        System.out.println("新增<username2,lfw2>的键值对：" + jedis.set("username2", "lfw2"));
        System.out.println("系统中所有的键如下");
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);
        System.out.println("删除键username2：" + jedis.del("username2"));
        System.out.println("判断键username2是否存在：" + jedis.exists("username2"));
        System.out.println("查看键username1所存储的值的类型：" + jedis.type("username1"));
        System.out.println("随机返回key空间的一个：" + jedis.randomKey());
        System.out.println("重命名key：" + jedis.rename("username1", "name"));
        System.out.println("取出改后的name：" + jedis.get("name"));
        System.out.println("按索引查询：" + jedis.select(0));
        System.out.println("返回当前数据库key的数目：" + jedis.dbSize());
    }
}
