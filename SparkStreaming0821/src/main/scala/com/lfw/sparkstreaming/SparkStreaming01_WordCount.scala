package com.lfw.sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming01_WordCount {
  def main(args: Array[String]): Unit = {
    //1.初始化 Spark 配置信息
    val sparkConf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")
    //2.初始化 SparkStreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    //3.通过监控端口创建 DStream,读进来的数据为一行
    val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop105", 9999)
    //将lineDStream按照空格切开
    val wordDStream: DStream[String] = lineDStream.flatMap(_.split(" "))
    //将wordDStream转换结构 word => (word,1)
    val word2oneDStream = wordDStream.map((_, 1))
    //将word2oneDStream按照key聚合
    val resultDStream = word2oneDStream.reduceByKey(_ + _)
    //将resultDStream进行控制台打印
    resultDStream.print()

    //TODO 3 启动StreamingContext,并且阻塞主线程,一直执行
    ssc.start()
    ssc.awaitTermination()
  }
}
