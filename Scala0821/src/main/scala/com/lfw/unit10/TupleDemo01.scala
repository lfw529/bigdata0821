package com.lfw.unit10

import scala.Tuple1

object TupleDemo01 {
  def main(args: Array[String]): Unit = {
    //创建
    //说明 1.tuple1 就是一个 Tuple 类型是 Tuple5
    //简单说明：为了高效的操作元组，编译器根据元素的个数不同，对应的不同类型
    //分别 Tuple1----Tuple22
    val tuple1 = (1,2,3,"hello",4)
    println(tuple1)
  }
}
