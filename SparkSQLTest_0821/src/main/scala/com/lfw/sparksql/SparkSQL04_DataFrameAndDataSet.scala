package com.lfw.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object SparkSQL04_DataFrameAndDataSet {
  def main(args: Array[String]): Unit = {
    // 1 创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQLTest")
    // 2 创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    //读取数据
    val df: DataFrame = spark.read.json("SparkSQLTest_0821/input/user.json")
    import spark.implicits._

    // DataFrame 转换为 DataSet
    val userDataSet: Dataset[User] = df.as[User]
    userDataSet.show()

    //4.3 DataSet 转换为 DataFrame
    val userDataFrame: DataFrame = userDataSet.toDF()
    userDataFrame.show()
    //5.关闭
    spark.stop()
  }
}
