package com.lfw.project01

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.{immutable, mutable}

object require02_top10Category_sessionTop10 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    //读取原始日志数据
    val lineRDD: RDD[String] = sc.textFile("SparkCoreTest_0821/input/user_visit_action.txt")

    //变换数据结构 将lineRDD => actionRDD
    val actionRDD: RDD[UserVisitAction] = lineRDD.map(
      line => {
        val datas: Array[String] = line.split("_")
        UserVisitAction(
          datas(0),
          datas(1),
          datas(2),
          datas(3),
          datas(4),
          datas(5),
          datas(6),
          datas(7),
          datas(8),
          datas(9),
          datas(10),
          datas(11),
          datas(12)
        )
      }
    )

    //3 用累加器实现
    //3.1 创建累加器
    val cateACC = new CategoryCountAccumulator

    //3.2 注册累加器
    sc.register(cateACC, "cateacc")

    //3.3 使用累加器
    actionRDD.foreach(
      action => cateACC.add(action)
    )

    //3.4 获取累加器的值
    val accMap: mutable.Map[(String, String), Long] = cateACC.value

    //4 按照品类 对 accMap 进行分组
    val groupMap: Map[String, mutable.Map[(String, String), Long]] = accMap.groupBy(_._1._1)

    //5 对 groupMap 进行结构变化 变成一个样例类的集合
    val infoIter: immutable.Iterable[CategoryCountInfo] = groupMap.map {
      case (id, map) => {
        val click = map.getOrElse((id, "click"), 0L)
        val order = map.getOrElse((id, "order"), 0L)
        val pay = map.getOrElse((id, "pay"), 0L)
        CategoryCountInfo(id, click, order, pay)
      }
    }

    //6 对这个样例类结合 倒序排序取前10
    val resultList: List[CategoryCountInfo] = infoIter.toList.sortWith(
      (info1, info2) => {
        if (info1.clickCount != info2.clickCount) {
          info1.clickCount > info2.clickCount
        } else if (info1.orderCount != info2.orderCount) {
          info1.orderCount > info2.orderCount
        } else {
          info1.payCount > info2.payCount
        }
      }
    ).take(10)

    resultList.foreach(println)

    println("-------------------需求2--------------------")
    //获取 Top10 热门品类
    val ids: List[String] = resultList.map(CategoryCountInfo => CategoryCountInfo.categoryId)
    //ids 设置成广播变量
    val bdIds: Broadcast[List[String]] = sc.broadcast(ids)
    //3 利用bdIds对原始数据做过滤,过滤出热门品类Top10的数据
    //在这里我们只要热门品类的点击数据,因为在咱们公司,一个会话只要有下单或者支付的数据,一定会有点击
    println(bdIds.value)
    //List(15, 2, 20, 12, 11, 17, 7, 9, 19, 13)
    val filterRDD: RDD[UserVisitAction] = actionRDD.filter(
      action => {
        //在这里我们只要热门品类的点击数据,因为在咱们公司,一个会话只要有下单或者支付的数据,一定会有点击
        if (action.click_category_id != "-1") {
          bdIds.value.contains(action.click_category_id)
        } else {
          false
        }
      }
    )

    filterRDD.collect().foreach(println)
    //4 对 filterRDD 进行数据的转换 UserVisitAction => (品类ID = 会话ID,1)
    val cateAndSessionToOneRDD = filterRDD.map(
      action => (action.click_category_id + "=" + action.session_id, 1)
    )

    //5 对 key 进行聚合（品类ID-会话ID,1） =>（品类ID = 会话ID,sum）
    val cateAndSessionToSumRDD: RDD[(String, Int)] = cateAndSessionToOneRDD.reduceByKey(_ + _)

    //6 对数据再次进行结构的变换（品类ID = 会话ID,sum） => （品类ID,(会话ID,sum)）
    val cateToSessionAndSumRDD: RDD[(String, (String, Int))] = cateAndSessionToSumRDD.map({
      case (key, sum) => {
        val keys = key.split("=")
        (keys(0), (keys(1), sum))
      }
    })

    //7 既然分开了品类 id 个会话id 那么我就可以按照品类id进行分组
    val groupRDD: RDD[(String, Iterable[(String, Int)])] = cateToSessionAndSumRDD.groupByKey()

    //8 对每个品类id下的会话按照会话的访问次数 进行倒序排序，取前10
    val resultRDD: RDD[(String, List[(String, Int)])] = groupRDD.mapValues(
      datas => datas.toList.sortBy(_._2)(Ordering[Int]).reverse.take(10))

    resultRDD.collect().foreach(println)
  }
}
