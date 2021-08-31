package com.lfw.unit10

import scala.collection.mutable

object MapDemo07 {
  def main(args: Array[String]): Unit = {
    val map7 = mutable.Map(("Alice", 10), ("Bob", 20), ("Kotlin", "北京"))
    println(map7.get("Alice").get)
//    println(map7.get("Alice~").get)  //抛出异常
  }
}
