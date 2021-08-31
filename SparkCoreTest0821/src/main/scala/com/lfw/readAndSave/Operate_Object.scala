package com.lfw.readAndSave
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Operate_Object {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[1]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    //3.1 创建RDD
    val dataRDD: RDD[Int] = sc.makeRDD(Array(1,2,3,4),2)
    //3.2 保存数据
    dataRDD.saveAsObjectFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\objFile")
    //3.3 读取数据
    sc.objectFile[Int]("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\objFile").collect().foreach(println)
    //4.关闭连接
    sc.stop()
  }
}
