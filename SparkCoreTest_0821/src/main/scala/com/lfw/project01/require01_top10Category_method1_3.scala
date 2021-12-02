package com.lfw.project01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object require01_top10Category_method1_3 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    //读取原始日志数据
    val actionRDD = sc.textFile("SparkCoreTest_0821/input/user_visit_action.txt")

    //2 转换数据结构
    //点击的数据  (品类id,(1,0,0))
    //下单的数据  (品类id,(0,1,0))
    //支付的数据  (品类id,(0,0,1))
    val mapRDD: RDD[(String, (Int, Int, Int))] = actionRDD.flatMap({
      action => {
        val datas: Array[String] = action.split("_")
        if (datas(6) != "-1") {
          //点击的数据
          List((datas(6), (1, 0, 0)))
        } else if (datas(8) != "null") {
          //下单的数据
          val arr: Array[String] = datas(8).split(",")
          arr.map((_, (0, 1, 0)))
        } else if (datas(10) != "null") {
          //支付的数据
          val arr: Array[String] = datas(10).split(",")
          arr.map((_, (0, 0, 1)))
        } else {
          Nil
        }
      }
    })

    mapRDD.collect().foreach(println)
    println("-------------------------")
    //3 按照key聚合,求出相同品类的点击次数 下单次数 支付次数
    val reduceRDD: RDD[(String, (Int, Int, Int))] = mapRDD.reduceByKey(
      (t1, t2) => (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
    )
    //倒序排序 取前10
    reduceRDD.sortBy(_._2, false).take(10).foreach(println)
    //TODO 3 关闭资源
    sc.stop()
  }
}
