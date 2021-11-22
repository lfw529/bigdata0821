package com.lfw.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object action11_foreach {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    //3具体业务逻辑
    //3.1 创建第一个RDD
    // val rdd: RDD[Int] = sc.makeRDD(List(1,2,3,4),2)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    //3.2 收集后打印
    rdd.collect().foreach(println) //在driver端打印，会按照List顺序打印
    println("****************")
    //3.3 分布式打印
    rdd.foreach(println) //在Executor端打印，由于分区数没有限定，所以是无序打印，只有当为1个分区时，有序

    //4.关闭连接
    sc.stop()
  }
}
