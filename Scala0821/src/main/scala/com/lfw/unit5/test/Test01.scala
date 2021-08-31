package com.lfw.unit5.test

import scala.io.StdIn

object Test01 {
  def main(args: Array[String]): Unit = {
    println("请输入一个数")
    val x = StdIn.readDouble()
    double_2(x)
  }

  def double_2(x: Double) {
    println(2 * x)
  }

  def AtoB(x: Double): Unit ={

  }


}
