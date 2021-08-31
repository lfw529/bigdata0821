package com.lfw.dependency
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Lineage03 {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //TODO 2 利用SparkConf创建sc对象
    //Application：初始化一个SparkContext即生成一个Application
    val sc = new SparkContext(conf)

    //textFile,flatMap,map算子全部是窄依赖,不会增加stage阶段
    val lineRDD: RDD[String] = sc.textFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\input\\1.txt")
    val flatMapRDD: RDD[String] = lineRDD.flatMap(_.split(" "))
    val mapRDD: RDD[(String, Int)] = flatMapRDD.map((_, 1))

    //reduceByKey算子会有宽依赖,stage阶段加1，2个stage,分区改成5
    val resultRDD: RDD[(String, Int)] = mapRDD.reduceByKey(_ + _, 5)

    //Job：一个Action算子就会生成一个Job，2个Job
    //job0打印到控制台
    resultRDD.collect().foreach(println)
    //job1输出到磁盘
    resultRDD.saveAsTextFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\output5")

    //阻塞线程,方便进入localhost:4040查看
    Thread.sleep(Long.MaxValue)

    //TODO 3 关闭资源
    sc.stop()
  }
}
