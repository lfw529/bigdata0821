package com.lfw.keyvalue
import org.apache.spark.{SparkConf, SparkContext}

object KeyValue07_sortByKey {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    //3
    val rdd = sc.makeRDD(Array((3,"aa"),(6,"cc"),(2,"bb"),(1,"dd")))
    //正序
    rdd.sortByKey(true).collect().foreach(println)
    println("-----------------------------")
    //倒序
    rdd.sortByKey(false).collect().foreach(println)

    //5.关闭
    sc.stop()
  }
}
