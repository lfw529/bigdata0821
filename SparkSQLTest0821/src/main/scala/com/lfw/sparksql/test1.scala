package com.lfw.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Encoder, Encoders, SparkSession}
import org.apache.spark.sql.expressions.Aggregator

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object test1 {
  def main(args: Array[String]): Unit = {
    //1.创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQLTest")
    //2.创建 SparkSession 对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    //释放资源
    spark.stop()

  }
}

/**
 * 自定义UDAF函数 city_remark
 * 输入:  city_name  String
 * 缓冲区:  (地区点击次数,Map(城市名称,城市点击次数))  Buffer
 * 输出:  北京21.2%，天津13.2%，其他65.6%   String
 *
 */

case class Buffer(var totalCnt: Long, var cityMap: mutable.Map[String, Long])

class CityRemarkUDAF extends Aggregator[String, Buffer, String] {
  //buffer的初始化
  override def zero: Buffer = Buffer(0L, mutable.Map[String, Long]())

  //单个分区内的聚合方法 buffer city_name
  override def reduce(buffer: Buffer, city: String): Buffer = {
    buffer.totalCnt += 1
    buffer.cityMap(city) = buffer.cityMap.getOrElse(city, 0L) + 1
    buffer
  }

  //分区间聚合方法  其实就是多个buffer进行聚合
  override def merge(b1: Buffer, b2: Buffer): Buffer = {
    //将b2的地区次数 加给b1
    b1.totalCnt += b2.totalCnt
    //遍历b2的城市Map,将b2的城市的点击次数 一个个的加给b1
    b2.cityMap.foreach {
      case (city, cityCnt) => {
        b1.cityMap(city) = b1.cityMap.getOrElse(city, 0L) + cityCnt
      }
    }
    //返回b1
    b1
  }

  override def finish(buffer: Buffer): String = {
    //0 创建一个listbuffer，返回最终结果
    val cityRemarkList = new ListBuffer[String]
    //1.取出buffer的cityMap，转成list,按照城市点击量倒序排序
    val cityOrderList: List[(String, Long)] = buffer.cityMap.toList.sortWith(
      (t1, t2) => t1._2 > t2._2
    )
    //2 取出list的前两名，求出前两名的百分比，放到一个listbuffer里面，准备放回
    var sum = 0L
    cityOrderList.take(2).foreach {
      case (city, cityCnt) => {
        val res: Long = cityCnt * 100 / buffer.totalCnt
        cityRemarkList.append(city + " " + res + "%")
        sum += res
      }
    }
    //3 求出第三个其他城市的占比
    if(cityOrderList.size > 2){
      cityRemarkList.append("其他 " + (100 - sum) + "%")
    }
    //4 返回cityRemarkList
    cityRemarkList.mkString(",")
  }
    override def bufferEncoder: Encoder[Buffer] = Encoders.product

    override def outputEncoder: Encoder[String] = Encoders.STRING

}