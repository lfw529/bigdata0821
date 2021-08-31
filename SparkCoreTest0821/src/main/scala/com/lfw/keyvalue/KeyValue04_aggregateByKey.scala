package com.lfw.keyvalue
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object KeyValue04_aggregateByKey {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //TODO 2 利用SparkConf创建sc对象
    val sc = new SparkContext(conf)
    val rdd: RDD[(String, Int)] = sc.makeRDD(List(("a", 1), ("a", 3), ("a", 5), ("b", 7), ("b", 2), ("b", 4), ("b", 6), ("a", 7)), 2)
    //    val rdd2: RDD[(String, Int)] = rdd.aggregateByKey(0)(Math.max, _ + _)//(b,13) (a,12)
    val rdd2: RDD[(String, Int)] = rdd.aggregateByKey(0)((k, v) => k + v, (k, v) => k + v)
    //简化版
    //val rdd2: RDD[(String, Int)] = rdd.aggregateByKey(0)(_ + _, _ + _)
    //    val rdd2: RDD[(String, Int)] = rdd.aggregateByKey(0)(Math.max,Math.max)
    rdd2.collect().foreach(println)

    //TODO 3 关闭资源
    sc.stop()
  }
}
