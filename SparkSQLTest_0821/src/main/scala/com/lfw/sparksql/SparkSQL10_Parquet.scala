package com.lfw.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object SparkSQL10_Parquet {
  def main(args: Array[String]): Unit = {
    //1.创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQLTest")
    //2.创建 SparkSession 对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    val peopleDF: DataFrame = spark.read.json("SparkSQLTest_0821/input/people.json")

    // peopleDF (DataFrame 对象) 保存为 parquet 文件时，依然会保留着结构信息(Schema)
    peopleDF.write.parquet("people.parquet")
    // 读取上述创建的 parquet 文件
    // Parquet 文件是自描述的，所以结构信息被保留
    // 读取一个 parquet 文件的结果是一个已具有完整结构信息的 DataFrame 对象
    val parquetFileDF: DataFrame = spark.read.parquet("people.parquet")
    // 除了上面所提到的直接在 Parquet 文件上进行 SQL 查询，Parquet 文件也可以用来创建一个临时视图，然后在 SQL 语句中使用
    parquetFileDF.createOrReplaceTempView("parquetFile")

    import spark.implicits._
    val namesDF: DataFrame = spark.sql("SELECT name FROM parquetFile WHERE age BETWEEN 13 AND 19")
    namesDF.map((attributes: Row) => "Name: " + attributes(0)).show()
  }
}
