package com.lfw.unit10

import scala.collection.mutable

object MapDemo09 {
  def main(args: Array[String]): Unit = {
    val map9 = mutable.Map(("Alice", 10), ("Bob", 20), ("Kotlin", "北京"))
    map9("A") = 666 //增加
    println("map9=" + map9)  //map9=Map(Bob -> 20, A -> 666, Kotlin -> 北京, Alice -> 10)
  }
}
