package com.lfw.unit4.myfor

object ForUntilDemo02 {
  def main(args: Array[String]): Unit = {
    //输出 10 句 "hello,lfw“
    val start = 1
    val end = 11
    //循环的范围是 start-----(end -1)
    for (i <- start until end) {
      println("hello,lfw")
    }
  }
}
