package com.lfw.partition

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object partition04_file {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    //1）输入数据1-4，每行一个数字；输出：0=>{1、2} 1=>{3} 2=>{4} 3=>{空}
    val rdd: RDD[String] = sc.textFile("SparkCoreTest_0821/input/2.txt", 3)

    rdd.saveAsTextFile("SparkCoreTest_0821/output4")

    //5.关闭
    sc.stop()
  }
}
