package com.lfw.jedis;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestTX {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.1.102", 6379);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "world");
        jsonObject.put("name", "lfw");
        //开启事务
        Transaction multi = jedis.multi();
        String result = jsonObject.toJSONString();

        try {
            multi.set("user1", result);
            multi.set("user2", result);
            int i = 1/0;   //代码抛出异常事务，执行失败
            multi.exec();//执行事务
        } catch (Exception e) {
            multi.discard();//放弃事务
        } finally {
            System.out.println(jedis.get("user1"));
            System.out.println(jedis.get("user2"));
            jedis.close();
        }
    }
}
