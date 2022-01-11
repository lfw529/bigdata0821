package com.lfw.flink.sink;

import com.lfw.flink.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

public class Sink_Redis {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //2.读取端口数据并转换为JavaBean
        SingleOutputStreamOperator<WaterSensor> waterSensorDS = env.socketTextStream("hadoop102", 7777)
                .map(new MapFunction<String, WaterSensor>() {
                    @Override
                    public WaterSensor map(String value) throws Exception {
                        String[] split = value.split(",");
                        return new WaterSensor(split[0],
                                Long.parseLong(split[1]),
                                Integer.parseInt(split[2]));
                    }
                });
        //3.将数据写入Redis
        FlinkJedisPoolConfig jedisPoolConfig = new FlinkJedisPoolConfig.Builder()
                .setHost("hadoop102")
                .setPort(6379)
                .build();
        waterSensorDS.addSink(new RedisSink<>(jedisPoolConfig, new MyRedisMapper()));
                    /*
                    key                 value(hash)
                   "sensor"               field           value
                    sensor             {"id":"ws_001","ts":1607527992000,"vc":20}
                    */
        //4.执行任务
        env.execute();
    }

    public static class MyRedisMapper implements RedisMapper<WaterSensor> {
        @Override
        public RedisCommandDescription getCommandDescription() {
            //返回存在 Redis 中的数据类型，存储的是Hash,第二个参数是外面的key
            return new RedisCommandDescription(RedisCommand.HSET, "sensor");
        }

        @Override
        public String getKeyFromData(WaterSensor data) {
            //从数据中获取 Key：Hash 的 Key
            return data.getId();
        }

        @Override
        public String getValueFromData(WaterSensor data) {
            //从数据中获取 Value:Hash 的 value
            return data.getVc().toString();
        }
    }
}
