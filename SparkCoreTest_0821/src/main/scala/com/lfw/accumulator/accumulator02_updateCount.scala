package com.lfw.accumulator

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

object accumulator02_updateCount {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    //3.创建RDD
    val dataRDD: RDD[(String, Int)] = sc.makeRDD(List(("a", 1), ("a", 2), ("a", 3), ("a", 4)))
    //3.1 定义累加器(框架自带的)
    val sum: LongAccumulator = sc.longAccumulator("sum")
    val value: RDD[(String, Int)] = dataRDD.map(t => {
      //3.2 累加器添加数据
      sum.add(1)
      t
    })
    //3.3 调用两次行动算子，map执行两次，导致最终累加器的值翻倍
    value.foreach(println) //+4
    value.collect() //+4
    //3.4 获取累加器的值
    println("a:" + sum.value) //8

    //4.关闭连接
    sc.stop()
  }
}
