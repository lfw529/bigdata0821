package com.lfw.partitioner

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object partitioner01_get {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")

    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    //3.创建 RDD
    val pairRDD: RDD[(Int, Int)] = sc.makeRDD(List((1, 1), (2, 2), (3, 3)))
    //3.1 打印分区器
    println(pairRDD.partitioner)
    //3.2 使用 HashPartitioner 对RDD进行重新分区
    val partitionRDD: RDD[(Int, Int)] = pairRDD.partitionBy(new HashPartitioner(2))
    //3.3 打印分区器
    println(partitionRDD.partitioner)
    //4.关闭连接
    sc.stop()
  }
}
