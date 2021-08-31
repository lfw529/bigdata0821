package com.lfw.sparkstreaming
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming05_Transform {
  def main(args: Array[String]): Unit = {
    //1 创建SparkConf
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    //2 创建StreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(3))
    //3.创建 DStream
    val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop102",9999)
    //在Driver端执行，全局一次
    println("111111111:" + Thread.currentThread().getName)
    //4.转换为 RDD 操作
    val wordToSumDStream = lineDStream.transform(
      rdd => {
          // 在Driver端执行(ctrl+n JobGenerator)，一个批次一次
          println("222222:" + Thread.currentThread().getName)
          val words: RDD[String] = rdd.flatMap(_.split(" "))
          val wordToOne = words.map(x=>{
            // 在Executor端执行，和单词个数相同
            println("333333:" + Thread.currentThread().getName)
            (x, 1)
          })

        val result: RDD[(String, Int)] = wordToOne.reduceByKey(_ + _)
        result
      }
    )

    //5 打印
    wordToSumDStream.print

    //6 启动
    ssc.start()
    ssc.awaitTermination()
  }
}
