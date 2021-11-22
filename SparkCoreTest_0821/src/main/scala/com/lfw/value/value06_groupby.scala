package com.lfw.value

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object value06_groupby {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    //3 具体业务逻辑
    //3.1 创建一个 RDD
    var rdd = sc.makeRDD(1 to 4, 2)
    //3.2 将每个分区的数据放到一个数组并收集到 Driver 端 打印
    val groupRDD: RDD[(Int, Iterable[Int])] = rdd.groupBy(int => {
      if (int % 2 == 0) 0
      else 1
    }) //非省略版， int % 2 本身就有 0，1 两个值
    //按奇偶分组
    rdd.groupBy(_ % 2).collect().foreach(println)
    println("----------------------------")
    //按大于2的分组
    val groupRDD2: RDD[(Int, Iterable[Int])] = rdd.groupBy(int => {
      if (int > 2) 1
      else 0
    })
    rdd.groupBy(_ > 2).collect().foreach(println)
    println("----------------------------")
    groupRDD.mapPartitionsWithIndex((index, iters) => iters.map((index, _))).collect().foreach(println)
    println("----------------------------")
    // 3.3 创建一个RDD
    val rdd1: RDD[String] = sc.makeRDD(List("hello", "hive", "hadoop", "spark", "scala"))
    // 3.4 按照首字母第一个单词相同分组
    rdd1.groupBy(str => str.substring(0, 1)).collect().foreach(println)

    Thread.sleep(Long.MaxValue)
    //5.关闭
    sc.stop()
  }
}
