package com.lfw.unit10

import scala.collection.mutable

object MapDemo04 {
  def main(args: Array[String]): Unit = {
    val map4 = mutable.Map(("Alice",10),("Bob",20),("Kotlin","北京"))
    println("map4=" +map4)
  }
}
