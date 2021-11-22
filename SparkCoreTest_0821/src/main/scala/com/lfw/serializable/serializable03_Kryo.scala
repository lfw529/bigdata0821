package com.lfw.serializable

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object serializable03_Kryo {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")

    //使用kryo序列化机制 的引入过程
    // 1.自定义的类还需要继承extends Serializable
    //2.设置spark.serializer为org.apache.spark.serializer.KryoSerializer
    //3.将你自定义的类 注册kryo序列化 .registerKryoClasses(Array(classOf[类名]))
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").registerKryoClasses(Array(classOf[Search2]))
    //TODO 2 利用SparkConf创建sc对象
    val sc = new SparkContext(conf)
    val rdd: RDD[String] = sc.makeRDD(Array("hello world", "hello spark", "hive", "atguigu"))

    val search2 = new Search2("hello")
    val rdd2: RDD[String] = search2.getMatch1(rdd)
    rdd2.collect().foreach(println) //hello world  //hello spark

    //TODO 3 关闭资源
    sc.stop()
  }
}

class Search2(query: String) extends Serializable {
  def isMatch(s: String): Boolean = {
    s.contains(query)
  }

  // 函数序列化案例
  def getMatch1(rdd: RDD[String]): RDD[String] = {
    rdd.filter(this.isMatch)
  }

  // 属性序列化案例
  def getMatch2(rdd: RDD[String]): RDD[String] = {
    //    val q = query
    rdd.filter(x => x.contains(this.query))
    //    rdd.filter(x => x.contains(q))
  }
}