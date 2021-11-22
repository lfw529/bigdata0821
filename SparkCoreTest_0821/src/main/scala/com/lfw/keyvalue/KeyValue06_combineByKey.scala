package com.lfw.keyvalue

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object KeyValue06_combineByKey {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    val list: List[(String, Int)] = List(("a", 88), ("b", 95), ("a", 91), ("b", 93), ("a", 95), ("b", 98))
    val rdd: RDD[(String, Int)] = sc.makeRDD(list, 2)

    //第一个参数 初始值  v => (v,1)  88 => (88,1)
    //第二个参数 分区内的计算逻辑   (88,1) 91 => (88+91,1+1)
    //第三个参数 分区间的计算逻辑   (179,2) (95,1) => (179+95,2+1)
    val combinRDD: RDD[(String, (Int, Int))] = rdd.combineByKey(
      v => (v, 1),
      (t: (Int, Int), v) => (t._1 + v, t._2 + 1),
      (t1: (Int, Int), t2: (Int, Int)) => (t1._1 + t2._1, t1._2 + t2._2)
    )
    combinRDD.collect().foreach(println)
    println("---------------------")
    val resultRDD: RDD[(String, Double)] = combinRDD.map {
      case (k, v) => (k, v._1.toDouble / v._2)
    }
    resultRDD.collect().foreach(println)

    //TODO 3 关闭资源
    sc.stop()
  }
}
