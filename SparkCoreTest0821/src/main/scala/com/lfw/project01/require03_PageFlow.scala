package com.lfw.project01
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object require03_PageFlow {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    //读取原始日志数据
    val lineRDD: RDD[String] = sc.textFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\input\\user_visit_action.txt")

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

    //因为 actionRDD 在下面用到了多次，最好提前做缓存
    actionRDD.cache()

    //3 定义一个 List 集合 用来过滤分母的数据
    val ids: List[String] = List("1", "2", "3", "4", "5", "6", "7")
    //1-2 2-3 3-4 4-5 5-6 6-7
    val zipIds: List[String] = ids.zip(ids.tail).map {
      case (p1, p2) => p1 + "-" + p2
    }

    //3.1 对ids创建广播变量
    val bdIds: Broadcast[List[String]] = sc.broadcast(ids)
    println(bdIds) //Broadcast(1)
    val bdZipIds: Broadcast[List[String]] = sc.broadcast(zipIds)
    println(bdZipIds) //Broadcast(2)
    println("---------------------")

    //4 求分母
    val idsArr = actionRDD
      //按照定义好的集合 过滤原始数据 只统计 1,2,3,4,5,6的页面的访问次数
      .filter(action => bdIds.value.init.contains(action.page_id))
      //转换数据结构 UserVisitAction => (页面id,1)
      .map(action => (action.page_id, 1))
      .reduceByKey(_ + _).collect()

    idsArr.foreach(println)
    val idsMap: Map[String, Int] = idsArr.toMap

    //5 求分子
    //5.1 先按照会话分组，将同一个会话的数据 聚合到一起
    val sessionGroupRDD: RDD[(String, Iterable[UserVisitAction])] = actionRDD.groupBy(_.session_id)
    //5.2 对 sessionGroupRDD 进行mapValues操作 处理同一个会话下面的 action
    val page2pageRDD: RDD[List[String]] = sessionGroupRDD.mapValues(
      datas => {
        //先对迭代器里面的 action 按照动作时间正序排序，防止乱序的情况存在
        val action: List[UserVisitAction] = datas.toList.sortBy(_.action_time)
        //转换数据结构 List[UserVisitAction] => List[页面ID]
        val pageIdList: List[String] = action.map(_.page_id)
        //拉链，形成页面单跳数组(1,2,3,4) zip(2,3,4)
        val page2page: List[(String, String)] = pageIdList.zip(pageIdList.tail)
        //对单跳元组 进行结构变换（1，2） => 1-2
        val pageJumpCounts = page2page.map {
          case (p1, p2) => p1 + "-" + p2
        }
        //最好过滤一下 因为我只需要 1-2 2-3 3-4 4-5 5-6 6-7 的数据
        pageJumpCounts.filter(
          str => bdZipIds.value.contains(str)
        )
      }
    ).map(_._2)
    page2pageRDD.collect().foreach(println)
    //聚合统计结果
    val reduceFlowRDD = page2pageRDD.flatMap(list => list).map((_, 1)).reduceByKey(_ + _)

    println("---------------分子--------------------")
    reduceFlowRDD.collect().foreach(println)
    println("---------------分母--------------------")
    println(idsMap)
    println("----------------页面单跳转换率---------------------")

    reduceFlowRDD.foreach {
      case (pageflow, cnt) => {
        //先把 pageflow 切开 是为了取分母
        val pageids: Array[String] = pageflow.split("-")
        //取分母
        val prePageCnt: Int = idsMap(pageids(0))

        //计算单跳转换率 = 分子 / 分母
        println(pageflow + " = " + cnt.toDouble / prePageCnt)
      }
    }

    //TODO 3 关闭资源
    sc.stop()
  }
}