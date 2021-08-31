package com.lfw.cache

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object checkpoint02 {

  def main(args: Array[String]): Unit = {

    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")

    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    //启动检查点功能前先设置一下检查点的存储目录
    //需要设置路径，否则抛异常：Checkpoint directory has not been set in the SparkContext
    sc.setCheckpointDir("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\checkpoint")

    //3. 创建一个RDD，读取指定位置文件:hello atguigu atguigu
    val lineRdd: RDD[String] = sc.textFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\input\\3.txt")

    //3.1.业务逻辑
    val wordRdd: RDD[String] = lineRdd.flatMap(line => line.split(" "))

    val wordToOneRdd: RDD[(String, Long)] = wordRdd.map {
      word => {
        (word, System.currentTimeMillis())
      }
    }

    //3.5 增加缓存，避免再重新跑一个job做checkpoint  //3次时间一样
    wordToOneRdd.cache()

    //3.4 数据检查点：针对wordToOneRdd做检查点计算
    wordToOneRdd.checkpoint()

    //3.2 触发执行逻辑
    wordToOneRdd.collect().foreach(println)
    /*
      (hello,1607342837127)
      (atguigu,1607342837127)
      (atguigu,1607342837127)
     */
    // 会立即启动一个新的job来专门的做checkpoint运算

    //3.3 再次触发执行逻辑
    wordToOneRdd.collect().foreach(println)
    /*
      (hello,1607342837127)
      (atguigu,1607342837127)
      (atguigu,1607342837127)
     */
    wordToOneRdd.collect().foreach(println)
    /*
      (hello,1607342837127)
      (atguigu,1607342837127)
      (atguigu,1607342837127)
    */
    Thread.sleep(10000000)

    //4.关闭连接
    sc.stop()
  }
}
