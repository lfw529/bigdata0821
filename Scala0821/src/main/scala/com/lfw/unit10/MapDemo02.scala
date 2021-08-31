package com.lfw.unit10

import scala.collection.mutable

object MapDemo02 {
  def main(args: Array[String]): Unit = {
    //1.从输出的结果看到，可变的 map 输出顺序和声明顺序不一致
    val map2 = mutable.Map("Alice" -> 10, "Bob" -> 20, "Kotlin" -> "北京")
    println(map2)
  }
}
