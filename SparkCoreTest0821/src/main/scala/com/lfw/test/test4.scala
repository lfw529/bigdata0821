package com.lfw.test

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object test4 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")

    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    val WordRdd: RDD[String] = sc.textFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\input\\1.txt")

    val lineRdd: RDD[String] = WordRdd.flatMap(_.split(" "))
    lineRdd.collect().foreach(println)
    println("-----------------------")
    val word2oneRdd: RDD[(String, Int)] = lineRdd.map((_, 1))
    word2oneRdd.collect().foreach(println)
    println("-----------------------")
    val rdd2: RDD[(String, Int)] = word2oneRdd.aggregateByKey(0)(_ + _, _ + _)
    rdd2.collect().foreach(println)
    println("-----------------------")
    val rdd3 = word2oneRdd.foldByKey(0)(_ + _)
    rdd3.collect().foreach(println)

  }
}
