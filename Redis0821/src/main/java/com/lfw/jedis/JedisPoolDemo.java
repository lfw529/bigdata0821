package com.lfw.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisPoolDemo {
    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("hadoop102", 6379);
        //获取jedis
        Jedis jedis = jedisPool.getResource();
        System.out.println(jedis.ping());
        jedis.close();
        jedisPool.close();
    }
}
