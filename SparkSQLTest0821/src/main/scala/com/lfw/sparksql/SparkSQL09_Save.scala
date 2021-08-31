package com.lfw.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

object SparkSQL09_Save {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")
    //TODO 2 利用SparkConf创建sparksession对象
    //通过设置spark.sql.sources.default这个参数 来控制sparksql读和存文件的默认数据格式
    conf.set("spark.sql.sources.default","json")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    //load 是 sparksql 通用的读取方法 而我并没有通过.format来设置文件格式，那就会走默认
    //正常情况下 默认格式 是 parquet 而我们是通过 spark.sql.sources.default 将默认格式设置为了 json
    //因此我们可以直接用 load 方法 读取 json 文件
    val df:DataFrame = spark.read.load("D:\\IdeaProjects\\bigdata0821\\SparkSQLTest0821\\input\\user.json")

    df.show()

    //write.save方法是spark通用的写方法
    //正常情况下默认格式为parquet ,而我们在上面通过conf文件设置了spark.sql.sources.default这个参数 为 json
    //因此 写出去的文件格式就变成了json

    //存储模式
    //append : 追加  如果目录不存在,直接写 如果目录存在  运行不报错  追加到已经存在的目录下
    //overwrite : 覆盖 如果目录不存在,直接写 如果目录存在 运行不报错  覆盖掉已经存在的目录
    // ignore : 忽略  如果目录不存在,直接写 如果目录存在  运行不报错  忽略本次运行 既不会追击 也不会覆盖
    //error" | "errorifexists" | "default 默认模式  如果目录不存在,直接写  如果目录存在  运行报错 目录已存在
    df.write.mode(SaveMode.ErrorIfExists).save("D:\\IdeaProjects\\bigdata0821\\SparkSQLTest0821\\out")

    import spark.implicits._
    val ds: Dataset[String] = df.map(
      row => row.mkString(",")
    )
    ds.write.format("text").save("D:\\IdeaProjects\\bigdata0821\\SparkSQLTest0821\\out2")

    spark.stop()
  }
}
