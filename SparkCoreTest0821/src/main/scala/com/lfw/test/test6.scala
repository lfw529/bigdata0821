package com.lfw.test

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object test6 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)
    val dataRDD: RDD[String] = sc.textFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\input\\agent.log")

    //string => (prv-adv,1)
    val prvandadv2oneRdd = dataRDD.map({
      line => {
        val r: Array[String] = line.split(" ")
        val s = (r(1) + "-" + r(4), 1)
        s
      }
    })

    //(prv-adv,1) => (prv-adv,sum)
    val prvandadv2sumRdd: RDD[(String, Int)] = prvandadv2oneRdd.reduceByKey((x, y) => x + y)
    //(prv-adv,sum）=>(prv,(adv,sum))
    val prv2advandsumRdd: RDD[(String, (String, Int))] = prvandadv2sumRdd.map({
      case (preandadv, sum) => {
        val ks: Array[String] = preandadv.split("-")
        val t: (String, (String, Int)) = (ks(0), (ks(1), sum))
        t
      }
    })
    //方式2
    val prv2advandsumRdd2: RDD[(String, (String, Int))] = prvandadv2sumRdd.map(
      t => {
        val preandadv: Array[String] = t._1.split("-")
        val w: (String, (String, Int)) = (preandadv(0), (preandadv(1), t._2))
        w
      }
    )
    //5 既然咱们把省份作为了key,那么就可以按照省份分组,将相同省份的广告以及广告点击次数放到一个组里面
    val groupRDD: RDD[(String, Iterable[(String, Int)])] = prv2advandsumRdd2.groupByKey()
    //6 对每个省的广告点击次数倒序排序 取前3
    // sortBy排序  默认升序排序  排完以后需要翻转
    val resultRDD = groupRDD.mapValues(
      iter => {
        iter.toList.sortBy(iter => iter._2).reverse.take(3)
      }
    )

    //方式2 ：sortWith排序 两两排序  传入一个left 和一个 right  大的在前 小的在后  降序排序
    val resultRDD2: RDD[(String, List[(String, Int)])] = groupRDD.mapValues(
      iter => {
        iter.toList.sortWith(
          (left, right) => left._2 > right._2
        ).take(3)
      }
    )

    resultRDD.collect().foreach(println)
    println("---------------------------")
    resultRDD2.collect().foreach(println)
    //关闭
    sc.stop()

  }
}
