package com.lfw.readAndSave
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Operate_Sequence {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[1]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    //3.1 创建rdd
    val dataRDD: RDD[(Int, Int)] = sc.makeRDD(Array((1,2),(3,4),(5,6)))
    //3.2 保存数据为SequenceFile
    dataRDD.saveAsSequenceFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\seqout")
    //3.3 读取SequenceFile文件
    sc.sequenceFile[Int,Int]("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\seqout").collect().foreach(println)

    //4.关闭连接
    sc.stop()
  }
}
