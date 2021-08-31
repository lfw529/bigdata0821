package com.lfw.unit10

import scala.collection.mutable

object MapDemo05 {
  def main(args: Array[String]): Unit = {
    val map5 = mutable.Map(("Alice",10),("Bob",20),("Kotlin","北京"))
    println(map5("Alice")) //10
    //抛出异常（java.util.NoSuchElementException:key not found:）
    println(map5("Alice~"))  //报错
  }
}
