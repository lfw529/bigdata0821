package com.lfw.unit10

object ListDemo02 {
  def main(args: Array[String]): Unit = {
    val list01 = List(1,2,3)
    //访问
    val value1 = list01(1)  //1 是索引，表示取出第2个元素
    println("value1=" +value1)
  }
}
