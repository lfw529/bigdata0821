package com.lfw.unit10

object TupleDemo03 {
  def main(args: Array[String]): Unit = {
    val t1 = (1, "a", "b", true, 2)
    for(item <- t1.productIterator){
      println("item=" + item)
    }
  }
}
