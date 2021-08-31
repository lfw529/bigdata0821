package com.lfw.test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object test2 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")

    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    val rdd1: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8), 2)

    val rdd2: RDD[Array[Int]] = rdd1.glom() //算子每次只能执行一步

    val rdd3: Array[Array[Int]] = rdd2.collect()

    rdd3.foreach(arr => println(arr.mkString(",")))
    println("-------------------------------------------")
    println(rdd3.foreach(arr => arr.mkString(",")))    //错误，应该每次都打印


    val maxrdd: RDD[Int] = rdd2.map(array => array.max)
    maxrdd.collect().foreach(println)

    //5.关闭
    sc.stop()

  }
}
