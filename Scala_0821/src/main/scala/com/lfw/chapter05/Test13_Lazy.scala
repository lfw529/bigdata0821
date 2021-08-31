package com.lfw.chapter05

object Test13_Lazy {
  def main(args: Array[String]): Unit = {
    lazy val result: Int = sum(13, 47)  //只会调用一次

    println("1. 函数调用")
    println("2. result = " + result)  //只会调用一次sum,且在第一次用到调用
    println("4. result = " + result)  //不会再调用sum
  }

  def sum(a: Int, b: Int): Int = {
    println("3. sum调用")
    a + b
  }
}
