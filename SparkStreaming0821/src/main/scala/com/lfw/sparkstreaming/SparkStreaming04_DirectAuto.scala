package com.lfw.sparkstreaming

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming04_DirectAuto {
  def main(args: Array[String]): Unit = {
    //1.创建 SparkConf
    val sparkConf:SparkConf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")
    //2.创建 StreamingContext
    val ssc = new StreamingContext(sparkConf,Seconds(3))
    //3.定义 Kafka参数：Kafka集群地址、消费者组名称、key序列化、value序列化
    val kafkaPara: Map[String, Object] = Map[String,Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "hadoop102:9092,hadoop103:9092,hadoop104:9092",
      ConsumerConfig.GROUP_ID_CONFIG -> "atguiguGroup",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )
    //4.读取 Kafka 数据创建 DStream
    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String,String](
      ssc,
      LocationStrategies.PreferConsistent,//优先位置
      ConsumerStrategies.Subscribe[String,String](Set("testTopic"),kafkaPara)//消费策略:(订阅多个主题，配置参数)
    )
    //5.将每条消息（KV）的V取出
    val valueDStream:DStream[String] = kafkaDStream.map(record => record.value())
    //6.计算 WordCount
    valueDStream.flatMap(_.split(" "))
      .map((_,1))
      .reduceByKey(_+_)
      .print()
    //7.开启任务
    ssc.start()
    ssc.awaitTermination()
  }
}
