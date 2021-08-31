package com.lfw.action
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object action10_save {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    //3具体业务逻辑
    //3.1 创建第一个RDD
    val rdd: RDD[Int] = sc.makeRDD(List(1,2,3,4), 2)
    //3.2 保存成Text文件
    rdd.saveAsTextFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\10_save_1")
    //3.3 序列化成对象保存到文件
    rdd.saveAsObjectFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\10_save_2")
    //3.4 保存成Sequencefile文件
    rdd.map((_,1)).saveAsSequenceFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\10_save_3")

    //4.关闭连接
    sc.stop()
  }
}
