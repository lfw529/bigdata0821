package com.lfw.unit10

import java.util
import scala.collection.mutable
/**
 * 集合互相转换
 */
object ScalaToJava {
  def main(args: Array[String]): Unit = {
    import scala.collection.JavaConverters._
    //将当前集合转换为Java中的List
    val javaC: util.List[Int] = List(1, 2, 3, 4).asJava
    println(javaC)
    //Java中集合转换为Scala中集合
    val scalaC: mutable.Buffer[Int] = javaC.asScala
    println(scalaC)
    //万能方法 toXXXX -> XX就是你要转换的集合
    scalaC.toList
  }
}
