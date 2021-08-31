package com.lfw.cache

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object cache02 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")

    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    //3.创建一个RDD,读取指定位置文件：hello atguigu atguigu
    val lineRdd: RDD[String] = sc.textFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\input\\3.txt")
    //3.1 业务逻辑
    val wordRdd = lineRdd.flatMap(line => line.split(" "))

    val wordToOneRdd: RDD[(String, Int)] = wordRdd.map {
      word => {
        println("*************")
        (word, 1)
      }
    }

    //采用 reduceByKey ,自带缓存
    //因为word2oneRDD调用了reduceByKey算子,底层要走shuffle
    //spark怕在shuffle过程,OOM发生错误,因此会自动对word2oneRDD启用缓存
    val wordByKeyRDD: RDD[(String, Int)] = wordToOneRdd.reduceByKey(_ + _)
    //3.5 cache 操作会增加血缘关系，不改变原有的血缘关系
    println(wordByKeyRDD.toDebugString)

    //3.4 数据缓存。
    //wordByKeyRDD.cache()

    //3.2 触发执行逻辑
    wordByKeyRDD.collect()

    println("-----------------")
    println(wordByKeyRDD.toDebugString)

    //3.3 再次触发执行逻辑
    wordByKeyRDD.collect()

    Thread.sleep(1000000)

    //4.关闭连接
    sc.stop()
  }
}
