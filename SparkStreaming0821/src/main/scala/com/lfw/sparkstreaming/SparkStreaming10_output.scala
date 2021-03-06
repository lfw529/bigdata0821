package com.lfw.sparkstreaming
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming10_output {
  def main(args: Array[String]): Unit = {
    // 1 初始化SparkStreamingContext
    val conf = new SparkConf().setMaster("local[*]").setAppName("sparkstreaming")
    val ssc = new StreamingContext(conf, Seconds(3))
    // 2 通过监控端口创建DStream，读进来的数据为一行行
    val lineDStream = ssc.socketTextStream("hadoop102", 9999)

    // 3 切割=》变换
    val wordToOneDStream = lineDStream.flatMap(_.split(" "))
      .map((_, 1))
    // 4 输出
    wordToOneDStream.foreachRDD(
      rdd=>{
        // 在Driver端执行(ctrl+n JobScheduler)，一个批次一次
        // 在JobScheduler 中查找（ctrl + f）streaming-job-executor
        println("222222:" + Thread.currentThread().getName)

        rdd.foreachPartition(
          //5.1 测试代码
          iter=>iter.foreach(println)

          //5.2 企业代码
          //5.2.1 获取连接
          //5.2.2 操作数据，使用连接写库
          //5.2.3 关闭连接
        )
      }
    )

    // 5 启动=》阻塞
    ssc.start()
    ssc.awaitTermination()
  }
}
