package com.lfw.sparksql
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSQL08_Load {
  def main(args: Array[String]): Unit = {
    //1.创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQLTest")
    //2.创建 SparkSession 对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    //sparksql 的数据读取
    //orc   parquet   schema   table   text   textFile
    val df: DataFrame = spark.read.json("D:\\IdeaProjects\\bigdata0821\\SparkSQLTest0821\\input\\user.json")
    df.show()
    val df2: DataFrame = spark.read.csv("D:\\IdeaProjects\\bigdata0821\\SparkSQLTest0821\\input\\user.txt")
    df2.show()

    //load方法 是通用方法读取
    val df3: DataFrame = spark.read.format("json").load("D:\\IdeaProjects\\bigdata0821\\SparkSQLTest0821\\input\\user.json")
    df3.show()
    //sparkSQL默认读取文件的格式 不是 textfile 也不是json文件  而是 parquet
    val df4: DataFrame = spark.read.load("D:\\IdeaProjects\\bigdata0821\\SparkSQLTest0821\\output\\part-00000-aedc93e0-5113-40be-acd7-ab3247cf23ed-c000.snappy.parquet")
    df4.show()

    val df5: DataFrame = spark.read.csv("D:\\IdeaProjects\\bigdata0821\\SparkSQLTest0821\\input\\user.txt")
    df5.show()
    //4 释放资源
    spark.stop()
  }
}
