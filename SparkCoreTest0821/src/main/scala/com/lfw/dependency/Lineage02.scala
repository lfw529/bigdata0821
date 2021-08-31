package com.lfw.dependency

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Lineage02 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")

    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    val fileRDD: RDD[String] = sc.textFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\input\\1.txt")
    println(fileRDD.dependencies)
    println("------------------")

    val wordRDD: RDD[String] = fileRDD.flatMap(str => str.split(" "))
    println(wordRDD.dependencies)
    println("------------------")

    val mapRDD: RDD[(String, Int)] = wordRDD.map(s => (s, 1))
    println(mapRDD.dependencies)
    println("-----------------")

    val resultRDD: RDD[(String, Int)] = mapRDD.reduceByKey(_ + _)
    println(resultRDD.dependencies)
    println("-----------------")

    resultRDD.collect()

    // 查看localhost:4040页面，观察DAG图
    Thread.sleep(10000000)

    //5.关闭
    sc.stop()

  }
}
