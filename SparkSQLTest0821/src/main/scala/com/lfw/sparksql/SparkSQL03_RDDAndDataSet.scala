package com.lfw.sparksql
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object SparkSQL03_RDDAndDataSet {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建 SparkConf 配置文件，并设置 App 名称
    val conf = new SparkConf().setAppName("SparkSqlTest").setMaster("local[*]")
    //TODO 2 利用 SparkConf 创建 sc 对象
    val sc = new SparkContext(conf)
    val lineRDD = sc.textFile("D:\\IdeaProjects\\bigdata0821\\SparkSQLTest0821\\input\\user.txt")
    // 普通rdd，数据只有类型，没有列名（缺少元数据）
    var rdd: RDD[(String, Long)] = lineRDD.map{
      line => {
        val field: Array[String] = line.split(",")
        (field(0),field(1).toLong)
      }
    }
    //TODO 3 利用 SparkConf 创建 sparksession 对象
    val spark = SparkSession.builder().config(conf).getOrCreate()
    //RDD 和 DF、DS 转换必须要导的包（隐式转换），spark 指的是上面的 sparkSession
    import spark.implicits._

    //TODO RDD => DS
    //普通 rdd 转 DS ,没办法补充元数据，因此一般不用(_1,_2)
    val ds: Dataset[(String, Long)] = rdd.toDS()
    ds.show()

    //样例类RDD,数据是一个个的样例类,有类型,有属性名(列名),不缺元数据
    val userRDD: RDD[User] = rdd.map{
      t=>{
        User(t._1,t._2)
      }
    }
    //样例类 RDD 转换 DS,直接 toDS 转换即可，不需要补充元数据，因此转 DS 一样要用样例类RDD
    val userDs: Dataset[User] = userRDD.toDS()
    userDs.show()

    //TODO DS=>RDD
    //ds 转成 rdd,直接.rdd即可，并且 ds 不会改变 rdd 里面的数据类型
    val rdd1: RDD[(String, Long)] = ds.rdd
    val userRDD2: RDD[User] = userDs.rdd

    //TODO 4 关闭资源
    sc.stop()
  }
}
