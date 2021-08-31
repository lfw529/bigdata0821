package com.lfw.value
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object value08_filter {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[Int] = sc.makeRDD(Array(1, 2, 3, 4), 2)
    //过滤出符合条件的数据
    /*val filterRdd: RDD[Int] = rdd.filter(int => {
      if (int % 2 == 0) true
      else false
    })*/
    //省略版
    val filterRdd: RDD[Int] = rdd.filter(_ % 2 == 0)
    //收集并打印数据
    filterRdd.collect().foreach(println)

    //关闭
    sc.stop()
  }
}
