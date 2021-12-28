package com.lfw.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import java.io.File

case class Record(key: Int, value: String)

object SparkSQL13_Hive {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "lfw")
    //TODO 1 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")
    //TODO 2 利用SparkConf创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()

    //warehouseLocation 指向托管数据库和表默认位置
    val warehouseLocation: String = new File("spark-warehouse").getAbsolutePath

    //实例化 SparkSession 对象时需通过 enableHiveSupport() 方法显示指定完全的 Hive 支持
    val sparkSession = SparkSession.builder()
      .appName("Spark Hive example")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    import spark.implicits._
    import spark.sql
    //通过 sql 接口在 Hive 中创建 src 表，并将指定位置的原始数据存储在 src Hive 表中
    sql("create table if not exists src (key INT, value STRING) USING hive")
    sql("Load data local inpath 'SparkSQLTest_0821/src/main/resources/kv1.txt' overwrite into table src")
    //使用 HiveQL 进行查询
    sql("select * from src").show
  }
}
