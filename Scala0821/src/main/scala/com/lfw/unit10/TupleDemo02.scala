package com.lfw.unit10

object TupleDemo02 {
  def main(args: Array[String]): Unit = {
    //访问元素
    val t1 = (1, "a", "b", true, 2)
    println(t1._1)  //1  //访问元组的第一个元素，从1开始

    /*
      override def productElement(n: Int) = n match {
      case 0 => _1
      case 1 => _2
      case 2 => _3
      case 3 => _4
      case 4 => _5
      case _ => throw new IndexOutOfBoundsException(n.toString())
      }
     */
  }
}
