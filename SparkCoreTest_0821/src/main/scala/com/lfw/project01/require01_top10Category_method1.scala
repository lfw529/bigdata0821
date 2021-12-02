package com.lfw.project01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object require01_top10Category_method1 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    //读取原始日志数据
    val actionRDD = sc.textFile("SparkCoreTest_0821/input/user_visit_action.txt")
    //先求出每个品类的点击次数（品类ID,点击次数）
    // 先过滤出所有的数据
    val clickActionRDD = actionRDD.filter(
      action => { //action 只是个别名，有 actionRDD 推出来是 String ,然后会以字符串形式进行
        val datas: Array[String] = action.split("_")
        datas(6) != "-1"
      }
    )
    //2.2 转换数据结构(品类id,1) => (品类id,sum)
    val clickCountRDD: RDD[(String, Int)] = clickActionRDD.map(
      action => {
        val datas: Array[String] = action.split("_")
        (datas(6), 1) //返回值类型,如果写在一行需要加分号
      }
    ).reduceByKey(_ + _)
    //3求出每个产品的下单次数(品类 ID,下单次数)
    val orderActionRDD: RDD[String] = actionRDD.filter(
      action => {
        val datas: Array[String] = action.split("_")
        datas(8) != "null"
      }
    )
    //数据结构转型
    val orderCountRDD: RDD[(String, Int)] = orderActionRDD.flatMap(
      action => {
        val datas: Array[String] = action.split("_")
        val arr: Array[String] = datas(8).split(",")
        arr.map((_, 1))
      }
    ).reduceByKey(_ + _)
    //4 求出每个品类的支付次数（品类ID,支付次数）
    val payActionRDD: RDD[String] = actionRDD.filter(
      action => {
        val datas: Array[String] = action.split("_")
        datas(10) != "null"
      }
    )
    //数据结构转型
    val payCountRDD: RDD[(String, Int)] = payActionRDD.flatMap(
      action => {
        val datas: Array[String] = action.split("_")
        val arr: Array[String] = datas(10).split(",")
        arr.map((_, 1))
      }
    ).reduceByKey(_ + _)

    //5.(品类ID,点击次数) (品类ID,下单次数)  (品类ID,支付次数)
    // (品类ID,(点击次数,下单次数,支付次数))
    val cogroupRDD: RDD[(String, (Iterable[Int], Iterable[Int], Iterable[Int]))] =
    clickCountRDD.cogroup(orderCountRDD, payCountRDD)

    cogroupRDD.collect().foreach(println)
    println("--------------------------")
    Iterable
    //转换 cogroupRDD 数据结构去掉迭代器
    val cogroupRDD2: RDD[(String, (Int, Int, Int))] = cogroupRDD.mapValues {
      case (iter1, iter2, iter3) => { //三元组的偏函数
        var clickCnt = 0
        val clickIter: Iterator[Int] = iter1.iterator //根据继承关系需要加 iterator
        if (clickIter.hasNext) {
          clickCnt = clickIter.next()
        }

        var orderCnt = 0
        val orderIter: Iterator[Int] = iter2.iterator
        if (orderIter.hasNext) {
          orderCnt = orderIter.next()
        }

        var payCnt = 0
        val payIter: Iterator[Int] = iter3.iterator
        if (payIter.hasNext) {
          payCnt = payIter.next()
        }

        (clickCnt, orderCnt, payCnt)
      }
    }

    cogroupRDD2.collect().foreach(println)
    //6 对 cogroupRDD2 进行倒序排序 取前10 热门品类Top10
    val result: Array[(String, (Int, Int, Int))] = cogroupRDD2.sortBy(tuple3 => tuple3._2, false).take(10)

    println("--------------------")
    result.foreach(println) //不用 collect 的原因是因为 此处的result 不是RDD 无法使用行动算子
    //关闭
    sc.stop()
  }
}
