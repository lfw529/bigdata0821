package com.lfw.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSQL11_Merging {
  def main(args: Array[String]): Unit = {
    //1.创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQLTest")
    //2.创建 SparkSession 对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    //引入 spark.implicits._用于将RDD隐式转换为 DataFrame
    import spark.implicits._
    //创建一个简单的DataFrame,包含value、square 两列,并将其存储到一个分区目录，该分区目录（key=1）表示额外的
    //分区的列为key,对应的值为1
    val squaresDF: DataFrame = spark.sparkContext.makeRDD(1 to 5).map(i => (i, i * i)).toDF("value", "square")
    squaresDF.write.parquet("SparkSQLTest_0821/data/test_table/key=1")
    //创建一个DataFrame，包含value、cube两列,并将其存储到相同表下新的分区目录
    //(data/test_table/key=2)，表示额外的分区列为key,对应的值为2
    //增加了一个cube列，去掉了一个已存在的 square列
    val cubesDF: DataFrame = spark.sparkContext.makeRDD(6 to 10).map(i => (i, i * i * i)).toDF("value", "cube");
    cubesDF.write.parquet("SparkSQLTest_0821/data/test_table/key=2")
    //读取完整的分区表，自动实现了两个分区（key=1/2）的合并
    val mergedDF: DataFrame = spark.read.option("mergeSchema", value = true).parquet("SparkSQLTest_0821/data/test_table")
    mergedDF.printSchema()
    //最终的 Schema 不仅包含两个 Parquet 分区文件出现的所有三列
    //还包含了作为分区目录的额外分区列 key
  }
}
