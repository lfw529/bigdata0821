package com.lfw.partition

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object partition01_Array_default {
  def main(args: Array[String]): Unit = {

    //1.创建 SparkConf 并设置 App 名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")

    //2.创建 SparkContext,该对象是提交 Spark App 的入口
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[Int] = sc.makeRDD(Array(1, 2, 3, 4))

    //3.输出数据，产生了8个分区
    rdd.saveAsTextFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\output")
    //4.关闭连接
    sc.stop()
  }
}
