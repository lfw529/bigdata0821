package com.lfw.unit13

object AnonymousTest {
  def main(args: Array[String]): Unit = {
    val f1=(n1:Int,n2:Int)=>{
      println("匿名函数被调用")
      n1 + n2
    }
    println("f1 类型=" + f1)
    println(f1(10,30))
  }
}
