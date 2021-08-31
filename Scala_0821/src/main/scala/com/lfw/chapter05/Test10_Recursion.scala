package com.lfw.chapter05

import scala.annotation.tailrec

object Test10_Recursion {
  def main(args: Array[String]): Unit = {
    println(fact(5))
    println(tailFact(5))
  }

  //递归实现计算阶乘
  def fact(n: Int): Int = {
    if (n == 0) return 1
    fact(n - 1) * n
  }

  //优化：尾递归实现,计算过程写在递归中，即上面的 *n 写在里面
  def tailFact(n: Int): Int = {
    @tailrec //该注解用于检验尾递归是否正确
    def loop(n: Int, currRes: Int): Int = {
      if (n == 0) return currRes
      loop(n - 1, currRes * n)
    }
    loop(n, 1)
  }
}
