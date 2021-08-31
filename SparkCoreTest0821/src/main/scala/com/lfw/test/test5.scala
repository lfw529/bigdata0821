package com.lfw.test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object test5 {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建spark程序配置文件
    val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    //TODO 2 利用sparkconf,创建spark上下文对象 sc
    val sc = new SparkContext(conf)

    val lineRdd: RDD[String] = sc.textFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\input\\1.txt")

    val wordRdd: RDD[String] = lineRdd.flatMap(line => line.split(" "))

    val word2oneRDD: RDD[(String, Int)] = wordRdd.map((_, 1))

    //第一个参数 初始值变化 num => num
    //第二个参数 分区内聚合  num + num
    //第三个参数 分区间聚合  num + num
    val resultRDD: RDD[(String, Int)] = word2oneRDD.combineByKey(
      num => num,
      (x: Int, y: Int) => x + y,
      (x: Int, y: Int) => x + y
    )

    resultRDD.collect().foreach(println)
    //TODO 3 关闭资源
    sc.stop()

  }
}
