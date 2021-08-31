package com.lfw.unit5.myexception

object ScalaExceptionDemo {
  def main(args: Array[String]): Unit = {

    try {
      val r = 10 / 0
    } catch {
      //1.在 scala 中只有一个 catch
      //2.在 catch 中有多个 case，每个 case 可以匹配一种异常 case ex:ArithmeticException
      //3.=> 关键符号，表示后面是对该异常的处理代码块
      //4.finally 最终要执行的
      case ex: ArithmeticException => {
        println("捕获了除数为0的算数异常")
      }
      case ex: Exception => {
        println("捕获了异常")
      }
    } finally {
      //最终要执行的代码
      println("scala finally...")
    }
    println("ok,继续执行----")
  }

}
