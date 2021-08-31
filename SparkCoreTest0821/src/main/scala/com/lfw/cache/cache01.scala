package com.lfw.cache
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object cache01 {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf: SparkConf = new SparkConf().setAppName("SparkCoreTest").setMaster("local[*]")

    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc: SparkContext = new SparkContext(conf)

    //3.创建一个 RDD,读取指定位置文件：hello atguigu atguigu
    val lineRdd = sc.textFile("D:\\IdeaProjects\\bigdata0821\\SparkCoreTest0821\\input\\3.txt")

    //3.1业务逻辑
    val wordRdd: RDD[String] = lineRdd.flatMap(line => line.split(" "))

    val wordToOneRdd = wordRdd.map {
      word => {  //懒加载
        println("**************")
        (word, 1)
      }
    }
    //3.5 cache 缓存前打印血缘关系
    println(wordToOneRdd.toDebugString)
    /*
    (2) MapPartitionsRDD[3] at map at cache01.scala:21 []
 |  MapPartitionsRDD[2] at flatMap at cache01.scala:19 []
 |  D:\IdeaProjects\bigdata0821\SparkCoreTest0821\input\3.txt MapPartitionsRDD[1] at textFile at cache01.scala:16 []
 |  D:\IdeaProjects\bigdata0821\SparkCoreTest0821\input\3.txt HadoopRDD[0] at textFile at cache01.scala:16 []
     */

    //3.4 数据缓存
    //cache 底层调用的就是 persist 方法，缓存级别默认用的是 MEMORY_ONLY
    wordToOneRdd.cache()

    //3.6 persist 方法可以更改存储级别
//     wordToOneRdd.persist(StorageLevel.MEMORY_AND_DISK_2)   //开启此行代码需要注掉 3.4 中的cache,只能设置一次存储级别

    //3.2 触发执行逻辑
    wordToOneRdd.collect().foreach(println)
    /*
    **************
    **************
    **************
    (hello,1)
    (atguigu,1)
    (atguigu,1)
     */

    //3.5 cache 缓存后打印血缘关系
    //cache 操作会增加血缘关系，不改变原有的血缘关系
    println(wordToOneRdd.toDebugString)
/*
(2) MapPartitionsRDD[3] at map at cache01.scala:21 [Memory Deserialized 1x Replicated]
 |       CachedPartitions: 2; MemorySize: 320.0 B; ExternalBlockStoreSize: 0.0 B; DiskSize: 0.0 B
 |  MapPartitionsRDD[2] at flatMap at cache01.scala:19 [Memory Deserialized 1x Replicated]
 |  D:\IdeaProjects\bigdata0821\SparkCoreTest0821\input\3.txt MapPartitionsRDD[1] at textFile at cache01.scala:16 [Memory Deserialized 1x Replicated]
 |  D:\IdeaProjects\bigdata0821\SparkCoreTest0821\input\3.txt HadoopRDD[0] at textFile at cache01.scala:16 [Memory Deserialized 1x Replicated]

 */
    println("=================================")

    //3.3 再次触发执行逻辑
    wordToOneRdd.collect().foreach(println)
    /*
      (hello,1)
      (atguigu,1)
      (atguigu,1)
     */
    Thread.sleep(Long.MaxValue)
    //5.关闭
    sc.stop()

  }
}
