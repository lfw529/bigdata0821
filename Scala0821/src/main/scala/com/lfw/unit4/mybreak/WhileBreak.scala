package com.lfw.unit4.mybreak

import util.control.Breaks._

object WhileBreak {
  def main(args: Array[String]): Unit = {

    var n = 1
    //breakable() 函数
    //说明
    //1.breakable 是一个高阶函数：可以接收函数的函数就是高阶函数

    //2.def breakable(op: => Unit): Unit =
    //    try op catch { case ex: BreakControl if ex eq breakException => }
    //(1)op:=> Unit 表示接受的参数是一个没有输入，也没有返回值的函数
    //(2)即可以简单理解可以接收一段代码
    //3.breakable 对 break() 抛出的异常做了处理，代码就继续执行
    //4.当我们传入的是代码块，scala 程序员会将() 改成{}
    breakable {
      while (n <= 20) {
        n += 1
        println("n=" + n)
        if (n == 18) {
          //中断 while
          //说明
          //1.在 scala 中使用函数式的 break 函数中断循环
          //2.def break():Nothing = {throw breakException }
          break()
        }
      }
    }
    println("ok--")
  }
}
