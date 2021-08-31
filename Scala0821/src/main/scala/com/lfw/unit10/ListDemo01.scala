package com.lfw.unit10

object ListDemo01 {
  def main(args: Array[String]): Unit = {
    //说明
    //1. 在默认情况下 List 是 scala.collection.immutable.List,即不可变
    //2. 在 Scala 中，List 就是不可变，如果需要使用可变的List ,则使用 ListBuffer
    //3. List 在 package object scala 做了 val List = scala.collection.immutable.List
    //4. val Nil = scala.collection.immutable.Nil //List()

    val list01 = List(1,2,3)//创建时，直接分配元素
    println(list01)  //List(1, 2, 3)
    val list02 = Nil  //空集合
    println(list02)  //List()
  }
}
