package com.lfw.sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming07_window {
  def main(args: Array[String]): Unit = {
    //1.初始化 SparkStreamingContext
    val conf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")
    val ssc = new StreamingContext(conf,Seconds(3))
    //2.通过监控端口创建 DStream,读进来的数据为一行行
    val lines = ssc.socketTextStream("hadoop102",9999)
    //3.切割=》变换
    val wordToOneDStream = lines.flatMap(_.split(" ")).map((_,1))
    // 4 获取窗口返回数据
    val wordToOneByWindow: DStream[(String, Int)] = wordToOneDStream.window(Seconds(12), Seconds(6))
    //5.聚合窗口数据并打印
    val wordToCountDStream: DStream[(String, Int)] = wordToOneByWindow.reduceByKey(_+_)
    wordToCountDStream.print()

    // 6 启动=》阻塞
    ssc.start()
    ssc.awaitTermination()
  }
}
