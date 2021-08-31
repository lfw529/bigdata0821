package com.lfw.dependency
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Lineage01 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    val fileRDD: RDD[String] = sc.textFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\input\\1.txt")
    println(fileRDD.toDebugString)
    println("-------------------")

    val wordRDD: RDD[String] = fileRDD.flatMap(_.split(" "))
    println(wordRDD.toDebugString)
    println("----------------------")

    val mapRDD: RDD[(String, Int)] = wordRDD.map((_,1))
    println(mapRDD.toDebugString)
    println("----------------------")

    val resultRDD: RDD[(String, Int)] = mapRDD.reduceByKey(_+_)
    println(resultRDD.toDebugString)

    resultRDD.collect()

    //5.关闭
    sc.stop()
  }
}
