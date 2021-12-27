package com.lfw.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._

object SparkSQL11_MySQL_Write {
  def main(args: Array[String]): Unit = {
    //1.创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQLTest")
    //2.创建 SparkSession 对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    // 3 准备数据
    // 注意：id是主键，不能和MySQL数据库中的id重复
    val rdd: RDD[User] = spark.sparkContext.makeRDD(List(User(30000, "zhangsan"), User(30001, "lisi")))

    import spark.implicits._
    val ds: Dataset[User] = rdd.toDS

    //4.向 MYSQL 中写入数据
    /**
     * 1.用sparksql往mysql里面写数据,如果表不存在,sparksql会自动帮我们创建表
     * 2.如果mysql里面表已经存在,可以用追加模式往里面写数据
     * 3.在公司里面慎用 复写模式,因为spark会删除已经存在的表,自己帮你重新创建一个新的同一个名字的表
     * 4.sparksql向mysql里面写数据,df列的顺序 和 mysql表的顺序 可以不一致,数据不会错乱
     * 5.sparksql向mysql里面写数据,列名很重要  mysql表的列名必须跟df的列名一致,才可以写进去
     * 6.如果mysql表的列名跟你df的列名真的不一致了,请修改df的列名
     * 7.如果mysql表的列的个数 比 df列的个数多,可以存,mysql里面多的列数据为null,如果mysql表的列的个数 比 df列的个数少,报错
     * 8.如果mysql表有主键的话,还需要考虑数据主键不能重复的问题,如果数据主键重复了,也会报错
     */
    ds.write.format("jdbc")
      .option("url","jdbc:mysql://hadoop102:3306/gmall")
      .option("driver","com.mysql.jdbc.Driver")
      .option("user","root")
      .option("password","123456")
      .option("dbtable","user_info")
      .mode(SaveMode.Append)
      .save()

    //释放资源
    spark.stop()
  }
  case class User(id: Int, name: String)
}
