package com.lfw.unit5.fundetails

object LazyDemo01 {
  def main(args: Array[String]): Unit = {
    lazy val res = sum(10,20)
    println("----------------------")
    println("res=" +res)//在要使用 res 前，才执行
  }
  /*运行结果
    ---------------------
    sum() 执行了..
    res=30
  */
  //sum 函数，返回和
  def sum(n1:Int,n2:Int):Int = {
    println("sum() 执行了..")  //输出一句话
    return n1 + n2
  }
}
