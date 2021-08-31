package com.lfw.unit10

import scala.collection.mutable

object SetDemo01 {
  def main(args: Array[String]): Unit = {
    val set = Set(1,2,3)//不可变
    println(set) //Set(1, 2, 3)

    val set2 = mutable.Set(1,2,"hello")  //可以变
    println("set2 = " +set2)  //set2 = Set(1, 2, hello)
  }
}
