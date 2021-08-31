package com.lfw.unit6.exam

object Test05 {
  def main(args: Array[String]): Unit = {
    var res: Long = 1
    for (i <- "Hello") { //字符串的本质是字符数组
      res *= i.toLong
    }
    println("res=" + res)
  }
}
