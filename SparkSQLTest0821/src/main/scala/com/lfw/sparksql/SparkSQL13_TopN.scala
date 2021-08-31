package com.lfw.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{Encoder, Encoders, SparkSession, functions}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object SparkSQL13_TopN {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")
    //TODO 2 利用SparkConf创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()

    //1 使用自定义udaf之前 先注册
    spark.udf.register("city_remark",functions.udaf(new CityRemarkUDAF))

    spark.sql(
      """
        |select
        |    t3.area,
        |    t3.product_name,
        |    t3.click_count,
        |    t3.city_remark
        |from
        |(
        |    select
        |        t2.area,
        |        t2.product_name,
        |        t2.click_count,
        |        t2.city_remark,
        |        rank() over(partition by t2.area order by t2.click_count desc) rk
        |    from
        |    (
        |        select
        |            t1.area,
        |            t1.product_name,
        |            count(*) click_count,
        |            city_remark(t1.city_name) city_remark
        |        from
        |        (
        |            select
        |                c.area,
        |                c.city_name,
        |                p.product_name,
        |                v.click_product_id
        |            from user_visit_action v
        |            join city_info c
        |            on v.city_id = c.city_id
        |            join product_info p
        |            on v.click_product_id = p.product_id
        |            where v.click_product_id > -1
        |        )t1
        |        group by t1.area,t1.product_name
        |    )t2
        |)t3
        |where t3.rk <= 3
        |""".stripMargin).show(1000,false)


    //TODO 3 关闭资源
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

case class Buffer(var totalCnt :Long,var cityMap:mutable.Map[String,Long])

class CityRemarkUDAF extends Aggregator[String,Buffer,String]{
  //buffer的初始化
  override def zero: Buffer = Buffer(0L,mutable.Map[String,Long]())

  //单个分区内的聚合方法 buffer city_name
  override def reduce(buffer: Buffer, city: String): Buffer = {
    buffer.totalCnt += 1
    buffer.cityMap(city) = buffer.cityMap.getOrElse(city,0L) + 1
    buffer
  }

  //分区间聚合方法  其实就是多个buffer进行聚合
  override def merge(b1: Buffer, b2: Buffer): Buffer = {
    //将b2的地区次数 加给b1
    b1.totalCnt += b2.totalCnt
    //遍历b2的城市Map,将b2的城市的点击次数 一个个的加给b1
    b2.cityMap.foreach{
      case (city,cityCnt) => {
        b1.cityMap(city) = b1.cityMap.getOrElse(city,0L) + cityCnt
      }
    }
    //返回b1
    b1

  }

  //最终返回的方法  逻辑计算方法
  override def finish(buffer: Buffer): String = {
    //0 创建一个listbuffer,返回最终结果
    val cityRemarkList = new ListBuffer[String]

    //1 取出buffer的cityMap,转成list,按照城市点击量倒序排序
    val cityOrderList: List[(String, Long)] = buffer.cityMap.toList.sortWith(
      (t1, t2) => t1._2 > t2._2
    )
    //2 取出list的前两名,求出前两名的百分比,放到一个listbuffer里面,准备放回
    var sum = 0L

    cityOrderList.take(2).foreach{
      case (city,cityCnt) => {
        val res : Long = cityCnt * 100 / buffer.totalCnt
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


