package com.lfw.project01

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.{immutable, mutable}


object require01_top10Category_method4 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    //读取原始日志数据
    val lineRDD = sc.textFile("SparkCoreTest_0821/input/user_visit_action.txt")

    //2 变换数据结构  将lineRDD => actionRDD
    val actionRDD: RDD[UserVisitAction] = lineRDD.map(
      line => {
        val datas: Array[String] = line.split("_")
        UserVisitAction( //将数据传递给样例类
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
    accMap.foreach(println)
    println("-------------------")
    //4 按照品类 对accMap进行分组
    val groupMap: Map[String, mutable.Map[(String, String), Long]] = accMap.groupBy(_._1._1)

    //5 对groupMap进行结构变化  变成了一个样例类的集合
    val infoIter: immutable.Iterable[CategoryCountInfo] = groupMap.map {
      case (id, map) => {
        val click = map.getOrElse((id, "click"), 0L)
        val order = map.getOrElse((id, "order"), 0L)
        val pay = map.getOrElse((id, "pay"), 0L)
        CategoryCountInfo(id, click, order, pay) //封装结果并返回
      }
    }

    //6 对这个样例类结合 倒序排序取前10
    /*  val resultList: List[CategoryCountInfo] = infoIter.toList.sortBy(info => (info.clickCount, info.orderCount, info.payCount))(Ordering[(Long, Long, Long)].reverse).take(10)
        resultList.foreach(println)
    */
    val resultList: List[CategoryCountInfo] = infoIter.toList.sortWith(
      (info1, info2) => {
        if (info1.clickCount > info2.clickCount) {
          true
        } else if (info1.clickCount < info2.clickCount) {
          false
        } else {
          if (info1.orderCount > info2.orderCount) {
            true
          } else if (info1.orderCount < info2.orderCount) {
            false
          } else {
            info1.payCount > info2.payCount
          }
        }
      }
    ).take(10)
    resultList.foreach(println)
  }
}

/**
 * 自定义累加器
 * 输入  : UserVisitAction
 * 输出 : mutable.Map[(String,String),Long]
 */
class CategoryCountAccumulator extends AccumulatorV2[UserVisitAction, mutable.Map[(String, String), Long]] {

  var map: mutable.Map[(String, String), Long] = mutable.Map[(String, String), Long]()

  override def isZero: Boolean = map.isEmpty

  override def copy(): AccumulatorV2[UserVisitAction, mutable.Map[(String, String), Long]] = new CategoryCountAccumulator

  override def reset(): Unit = map.clear()

  override def add(action: UserVisitAction): Unit = {

    if (action.click_category_id != "-1") {
      //点击的数据 key = (cid,"click"),以这种形式累加
      val key = (action.click_category_id, "click")
      map(key) = map.getOrElse(key, 0L) + 1L
    } else if (action.order_category_ids != "null") {
      //下单的数据  key = (cid,"order")
      val cids: Array[String] = action.order_category_ids.split(",")
      for (cid <- cids) {
        val key = (cid, "order")
        map(key) = map.getOrElse(key, 0L) + 1L
      }
    } else if (action.pay_category_ids != "null") {
      //支付的数据  key = (cid,"order")
      val cids: Array[String] = action.pay_category_ids.split(",")
      for (cid <- cids) {
        val key = (cid, "pay")
        map(key) = map.getOrElse(key, 0L) + 1L
      }
    }

  }

  override def merge(other: AccumulatorV2[UserVisitAction, mutable.Map[(String, String), Long]]): Unit = {
    other.value.foreach {
      case (key, cnt) => {
        map(key) = map.getOrElse(key, 0L) + cnt
      }
    }
  }

  override def value: mutable.Map[(String, String), Long] = map
}
