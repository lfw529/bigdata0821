package com.lfw.unit11

object FlatMapDemo01 {
  def main(args: Array[String]): Unit = {
    val names = List("Alice", "Bob", "Nick")

    //需求是将 List 集合中的所有元素，进行扁平化操作，即把所有元素打散
    val names2 = names.flatMap(upper)
    println("names2=" + names2)  //names2=List(A, L, I, C, E, B, O, B, N, I, C, K)
  }

  def upper(s:String):String = {
    s.toUpperCase
  }
}
