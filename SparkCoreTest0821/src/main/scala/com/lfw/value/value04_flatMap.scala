package com.lfw.value

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object value04_flatMap {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //TODO 2 利用SparkConf创建sc对象
    val sc = new SparkContext(conf)
    val listRDD = sc.makeRDD(List(List(1, 2), List(3, 4), List(5, 6), List(7)), 2)
    listRDD.mapPartitionsWithIndex(
      (index, iters) => iters.map(a => (index, a))
    ).collect().foreach(println)
    println("------------------")
    val rdd2: RDD[Int] = listRDD.flatMap(list => list)
    val rdd3: RDD[Int] = listRDD.flatMap(list => list.map(_ + 1))

    rdd2.mapPartitionsWithIndex(
      (index, iters) => iters.map((index, _))
    ).collect().foreach(println)
    println("------------------")
    rdd3.collect().foreach(println)
    println("------------------")
    val strRDD: RDD[String] = sc.makeRDD(List("hello atguigu", "hello spark"), 2)

    strRDD.mapPartitionsWithIndex(
      (index, iters) => iters.map((index, _))
    ).collect().foreach(println)
    println("------------------")
    val wordRDD: RDD[String] = strRDD.flatMap(_.split(" "))

    wordRDD.mapPartitionsWithIndex(
      (index, iters) => iters.map((index, _))
    ).collect().foreach(println)

    //TODO 3 关闭资源
    sc.stop()
  }
}
