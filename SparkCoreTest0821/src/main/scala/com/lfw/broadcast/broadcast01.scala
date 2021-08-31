package com.lfw.broadcast
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object broadcast01 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    //3.创建一个字符串RDD，过滤出包含WARN的数据
    val rdd: RDD[String] = sc.makeRDD(List("WARN:Class Not Find", "INFO:Class Not Find", "DEBUG:Class Not Find"), 4)
    val str: String = "WARN"

    // 声明广播变量
    val bdStr: Broadcast[String] = sc.broadcast(str)

    val filter: RDD[String] = rdd.filter {
      // log=>log.contains(str)
      log => log.contains(bdStr.value)
    }
    filter.foreach(println)

    //4.关闭连接
    sc.stop()
  }
}
