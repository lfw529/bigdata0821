package com.lfw.jedis;

import redis.clients.jedis.Jedis;

public class TestConnect {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.1.102",6379);

        //执行ping命令
        String ping = jedis.ping();
        System.out.println(ping);
        jedis.close();
    }
}
