package com.lfw.unit10

import scala.collection.mutable

object MapDemo08 {
  def main(args: Array[String]): Unit = {
    val map8 = mutable.Map(("Alice", 10), ("Bob", 20), ("Kotlin", "北京"))
    println(map8.getOrElse("Alice~~~", "默认的值 鱼 <.)))><<")) //默认的值 鱼 <.)))><<
  }
}
