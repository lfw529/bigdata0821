package com.lfw.unit6.exam

import scala.language.postfixOps

object Test04 {
  def main(args: Array[String]): Unit = {
    /*
      编写一个过程countdown(n:Int)，打印从n到0的数字
     */
    def countdown(n: Int): Unit = {
      for (i <- 0 to n reverse) {
        println(i)
      }
    }

    def countdown2(n: Int): Unit = {
      (0 to n).reverse.foreach(println)
    }

    val n = 3;
    val res1 = countdown2(3);
    //foreach
    //foreach 函数（f: Int => u）,即接收一个输入参数为 Int,输出参数为 u 的函数
    //说明
    //1.将 res1 的每个元素依次遍历出来，传递给println(x)
  }
}
