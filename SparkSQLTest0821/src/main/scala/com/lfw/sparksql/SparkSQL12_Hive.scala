package com.lfw.sparksql
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSQL12_Hive {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME","lfw")
    //TODO 1 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")
    //TODO 2 利用SparkConf创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()

    spark.sql("show tables").show()
    spark.sql("show databases").show()

//    spark.sql("create table student(id int,name String)").show()
    spark.sql("select * from student").show()
    spark.sql("insert into table student values(4,'zhangsan')")
    spark.sql("select * from student").show()

    /**
     * 用idea写sparkSQL代码连接外部Hive 步骤
     * 1.在项目的pom文件里面添加依赖 spark-sql  spark-hive  mysql的连接驱动依赖
     * 2.在项目的类路径下 放一个hive-site.xml的配置文件
     * 3.创建SparkSession的时候,需要获得外部hive的支持  .enableHiveSupport()
     */

    //TODO 3 关闭资源
    spark.stop()
  }
}
