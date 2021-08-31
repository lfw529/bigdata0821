package com.lfw.unit6.exam

object Test02 {
  /*
    空的块表达式{}的值是什么？类型是什么？
   */
  def main(args: Array[String]): Unit = {
    val t = {}
    println("t=" + t + " " + t.isInstanceOf[Unit])
  }
}
