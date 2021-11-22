package com.lfw.value

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object value05_glom {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //TODO 2 利用SparkConf创建sc对象
    val sc = new SparkContext(conf)
    val rdd: RDD[Int] = sc.makeRDD(Array(1, 2, 3, 4), 2)

    val rdd2: RDD[Array[Int]] = rdd.glom()

    val array: Array[Array[Int]] = rdd2.collect()

    for (elem <- array) {
      println(elem.mkString(","))
    }
    println("-------------------")
    val maxRDD: RDD[Int] = rdd2.map(arr => arr.max)
    maxRDD.collect().foreach(println)
    println(maxRDD.sum())

    //TODO 3 关闭资源
    sc.stop()
  }
}
