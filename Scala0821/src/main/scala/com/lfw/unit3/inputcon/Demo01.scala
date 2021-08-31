package com.lfw.unit3.inputcon

import scala.io.StdIn

object Demo01 {
  def main(args: Array[String]): Unit = {
    //要求：可以从控制台接收用户信息，【姓名，年龄，薪水】。

    println("请输入姓名：")
    val name = StdIn.readLine()
    println("请输入年龄：")
    val age = StdIn.readInt()
    println("请输入薪水：")
    val sal = StdIn.readDouble()
    printf("用户的信息为：name=%s age=%d sal= %.2f", name, age, sal)
  }
}
