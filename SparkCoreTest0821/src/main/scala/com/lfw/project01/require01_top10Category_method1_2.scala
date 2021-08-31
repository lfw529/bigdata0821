package com.lfw.project01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object require01_top10Category_method1_2 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    //读取原始日志数据
    val actionRDD = sc.textFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\input\\user_visit_action.txt")

    //actionRDD 在下面用到了多次，最好提前缓存下
    actionRDD.cache()

    //1先求出每个品类的点击次数（品类ID,点击次数）
    //2.1先过滤出所有点击的数据
    val clickActionRDD: RDD[String] = actionRDD.filter(
      action => {
        val datas: Array[String] = action.split("_")
        datas(6) != "-1"
      }
    )
    //2.2转换数据结构（品类id,1） => （品类id,sum）
    val clickCountRDD: RDD[(String, Int)] = clickActionRDD.map(
      action => {
        val datas: Array[String] = action.split("_")
        (datas(6), 1)
      }
    ).reduceByKey(_ + _)

    //3 求出每个品类的下单次数 (品类ID,下单次数)
    val orderActionRDD: RDD[String] = actionRDD.filter(
      action => {
        val datas: Array[String] = action.split("_")
        datas(8) != "null"
      }
    )

    val orderCountRDD: RDD[(String, Int)] = orderActionRDD.flatMap(
      action => {
        val datas: Array[String] = action.split("_")
        val arr: Array[String] = datas(8).split(",")
        arr.map((_, 1))
      }
    ).reduceByKey(_ + _)

    //4 求出每个品类的支付次数 (品类ID,支付次数)
    val payActionRDD: RDD[String] = actionRDD.filter(
      action => {
        val datas: Array[String] = action.split("_")
        datas(10) != "null"
      }
    )

    val payCountRDD: RDD[(String, Int)] = payActionRDD.flatMap(
      action => {
        val datas: Array[String] = action.split("_")
        val arr: Array[String] = datas(10).split(",")
        arr.map((_, 1))
      }
    ).reduceByKey(_ + _)

    //5 利用union all的方式 实现满外连接  前提是需要提前补0
    // (品类ID,点击次数)  => (品类ID,(点击次数,0,0))
    // (品类ID,下单次数)  => (品类ID,(0,下单次数,0))
    // (品类ID,支付次数)  => (品类ID,(0,0,支付次数))
    //(品类ID,(点击次数,下单次数,支付次数))
    //cogourp算子底层会走多次shuffle,效率低
    val clickRDD: RDD[(String, (Int, Int, Int))] = clickCountRDD.map({
      case (id, cnt) => (id, (cnt, 0, 0))
    })

    val orderRDD: RDD[(String, (Int, Int, Int))] = orderCountRDD.map({
      case (id, cnt) => (id, (0, cnt, 0))
    })

    val payRDD: RDD[(String, (Int, Int, Int))] = payCountRDD.map({
      case (id, cnt) => (id, (0, 0, cnt))
    })

    val unionRDD: RDD[(String, (Int, Int, Int))] = clickRDD.union(orderRDD).union(payRDD)
    unionRDD.collect().foreach(println)

    println("----------------------")
    val unionRDD2: RDD[(String, (Int, Int, Int))] = unionRDD.reduceByKey(
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
      }
    )
    //6 对 unionRDD2 进行倒序排序 取前10，热门品类 Top10
    val result: Array[(String, (Int, Int, Int))] = unionRDD2.sortBy(_._2, false).take(10)
    result.foreach(println)
  }
}
