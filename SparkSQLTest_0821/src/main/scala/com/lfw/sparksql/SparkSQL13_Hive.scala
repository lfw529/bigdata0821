package com.lfw.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
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
    //    val warehouseLocation: String = new File("spark-warehouse").getAbsolutePath

    //实例化 SparkSession 对象时需通过 enableHiveSupport() 方法显示指定完全的 Hive 支持
    val sparkSession = SparkSession.builder()
      .appName("Spark Hive example")
      .config(conf)
      .enableHiveSupport()
      .getOrCreate()

    import spark.implicits._
    import spark.sql
    //通过 sql 接口在 Hive 中创建 src 表，并将指定位置的原始数据存储在 src Hive 表中
    sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) row format delimited fields terminated by '\t'") //一定要限定分隔符
    sql("Load data local inpath 'SparkSQLTest_0821/src/main/resources/kv1.txt' overwrite into table src")
    //sql("insert into table src values(1,'wangwu'),(2,'zhaoliu')")
    //使用 HiveQL 进行查询
    sql("select * from src").show
    //+---+-------+
    //|key|  value|
    //+---+-------+
    //|238|val_238|
    //| 86| val_86|
    //|311|val_311|

    //包含 Hive 聚合函数 count() 的查询依然被支持
    sql("SELECT COUNT(*) FROM src").show()
    //+--------+
    //|count(1)|
    //+--------+
    //|       8|
    //+--------+

    //SQL 查询的结果本身就是 DataFrame，并支持所有正常的功能
    val sqlDF: DataFrame = sql("SELECT key, value from src where key < 10 order by key")
    //DataFrame 中的元素是 Row 类型的，允许按顺序访问每个列。
    val stringsDS: Dataset[String] = sqlDF.map {
      case Row(key: Int, value: String) => s"Key: $key, Value: $value"
    }

    stringsDS.show()
    //+-----+
    //|value|
    //+-----+
    //+-----+

    //也可以使用 DataFrame 在 SparkSession 中创建临时视图
    val recordsDF: DataFrame = sparkSession.createDataFrame((1 to 100).map(i => Record(i, s"val_$i")))
    recordsDF.createOrReplaceTempView("records")
    //sql 查询中可以对 DataFrame 注册的临时表和 Hive 表执行 Join 连接操作
    sql("select * from records r join src s on r.key =s.key").show()
    //+---+------+---+------+
    //|key| value|key| value|
    //+---+------+---+------+
    //| 27|val_27| 27|val_27|
    //| 86|val_86| 86|val_86|
    //+---+------+---+------+
  }
}
