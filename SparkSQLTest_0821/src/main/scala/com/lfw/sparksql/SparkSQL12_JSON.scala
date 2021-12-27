package com.lfw.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSQL12_JSON {
  def main(args: Array[String]): Unit = {
    //1.创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQLTest")
    //2.创建 SparkSession 对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    //通过导入 spark.implicits._,来支持自动生成原始类型（Int,String）和（Product）类型（Case类）的编码器来完成 DataFrame 的生成
    import spark.implicits._
    //JSON 数据集通过存储路径进行指定
    //路径可以是单个文件或存储文件文本的目录
    val path = "SparkSQLTest_0821/input/people.json"
    val peopleDF: DataFrame = spark.read.json(path)
    //推断得到的 Schema 可以使用 printSchema() 方法来显示
    peopleDF.printSchema()
    //root
    // |-- age: long (nullable = true)
    // |-- institute: string (nullable = true)
    // |-- name: string (nullable = true)
    // |-- phone: long (nullable = true)
    // |-- sex: string (nullable = true)

    // 使用 DataFrame 创建一个临时视图
    peopleDF.createOrReplaceTempView("people")
    // SQL 语句可以通过使用 spark 提供的 sql 方法来运行
    val teenagerNameDF: DataFrame = spark.sql("select name from people where age between 13 and 19")
    teenagerNameDF.show()
    //+----+
    //|name|
    //+----+
    //|Fvdy|
    //|Ywdy|
    //|Vvdy|
    //|Ccdy|
    //|Hkdy|
    //|Iody|
    //|Uudy|
    //|Ttdy|
    //|Tydy|
    //|Qxdy|
    //+----+
    //或者，可以为由 DataSet
  }
}
