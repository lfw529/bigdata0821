package com.lfw.unit4.mywhile

object Demo01 {
  def main(args: Array[String]): Unit = {
    var i = 0 //for
    do {
      printf(i + " hello,lfw\n")
      i += 1
    } while (i < 10)
  }
}
