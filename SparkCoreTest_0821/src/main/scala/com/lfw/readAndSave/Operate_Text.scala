package com.lfw.readAndSave

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Operate_Text {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[1]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    //3.1 读取输入文件
    val inputRDD: RDD[String] = sc.textFile("SparkCoreTest_0821/input/1.txt")
    //3.2 保存数据
    inputRDD.saveAsTextFile("SparkCoreTest_0821/textFile")

    //4.关闭连接
    sc.stop()
  }
}
