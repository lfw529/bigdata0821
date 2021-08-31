package com.lfw.unit11

object FilterDemo01 {
  def main(args: Array[String]): Unit = {
    //选出首字母为 A 的元素
    val names = List("Alice","bob","Nick")
    val names2 = names.filter(startA)
    println("names=" + names)  //names=List(Alice, bob, Nick)
    println("names2=" +names2)  //names2=List(Alice)
  }
  def startA(str: String):Boolean = {
    str.startsWith("A")
  }
}
