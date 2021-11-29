package com.lfw.accumulator

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object accumulator01_system {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建 SparkConf 配置文件，并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //TODO 2 利用SparkConf 创建sc对象
    val sc = new SparkContext(conf)
    val dataRDD: RDD[(String, Int)] = sc.makeRDD(List(("a", 1), ("a", 2), ("a", 3), ("a", 4)), 2)
    //需求:统计a出现的所有次数 ("a",10)
    //普通算子实现 reduceByKey算子会走shuffle 效率低
    val rdd: RDD[(String, Int)] = dataRDD.reduceByKey(_ + _)
    rdd.collect().foreach(println) // (a,10)
    //结论：普通变量实现 无法实现这个功能 因为普通变量sum 只能从driver端传给executor端，而不能从executor返回到driver
    var sum = 0
    dataRDD.foreach({
      case (a, count) => {
        sum += count
        println("sum = " + sum) //1 3 3 7 ,因为有两个分区
      }
    })
    println("sum=" + sum) //0

    //累加器实现
    //1 创建累加器
    val accSum = sc.longAccumulator("accSum") //括号中的名字是自己定义的，可以随便写
    dataRDD.foreach({
      case (a, count) => {
        //使用累加器
        accSum.add(count)
        //不要在 executor 端查看累加器的值，是因为查看到的值不对
        //因此我们才叫累加器为分布式共享只写变量
        println("accSum = " + accSum.value) //1 3 3 7
      }
    })
    //3 获取累加器的值
    println(("a", accSum.value)) //10

    //4.关闭连接
    sc.stop()
  }
}

