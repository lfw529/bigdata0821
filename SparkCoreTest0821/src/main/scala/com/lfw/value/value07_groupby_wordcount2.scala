package com.lfw.value
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object value07_groupby_wordcount2 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc = new SparkContext(conf)
    //3具体业务逻辑
    // 3.1 创建一个RDD
    val strList: List[String] = List("Hello Scala", "Hello Spark", "Hello World")
    val rdd = sc.makeRDD(strList)
    //先拍平
    val wordRDD = rdd.flatMap(_.split(" "))
    //直接按照单词分组
    val groupRDD: RDD[(String, Iterable[String])] = wordRDD.groupBy(word => word)
    groupRDD.collect().foreach(println)
    println("---------------------------")
    val rdd1: RDD[(String, Int)] = groupRDD.map(
      t => (t._1, t._2.toList.size)
    )
    val rdd2 = groupRDD.map {
      case (k, v) => (k, v.toList.size)
    }
    //打印输出
    rdd1.collect().foreach(println)
    println("---------------------------")
    rdd2.collect().foreach(println)
    //关闭资源
    sc.stop()
  }
}
