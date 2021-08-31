package com.lfw.unit5.fundetails

object Details03 {
  def main(args: Array[String]): Unit = {
    def f1(): Unit = {
      println("f1")
    }

    println("ok--")
    //只是声明，并未使用
    def sayOk(): Unit = { //private final sayOk$1()
      println("main sayOk")

      def sayOk(): Unit = { //private final sayOk$2()
        println("sayok sayok")
      }
    }

  }
  def sayOk(): Unit = { //成员
    println("main sayOk")
  }

}
