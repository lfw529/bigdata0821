package com.lfw.unit10

import scala.collection.mutable

object MapDemo06 {
  def main(args: Array[String]): Unit = {
    val map6 = mutable.Map(("Alice", 10), ("Bob", 20), ("Kotlin", "北京"))
    if(map6.contains("Alice")){
      println("key 存在，值="+ map6("Alice"))  //key 存在，值=10
    }else{
      println("key 不存在:)")
    }
  }
}
