package com.lfw.unit4.ifelse

import scala.io.StdIn

object Exercise04 {
  def main(args: Array[String]): Unit = {
    /*出票系统：根据淡旺季的月份和年龄，
      打印票价
      4_10 旺季：
        成人（18-60）：60
        儿童（<18）:半价
        老人（>60）:1/3
      淡季：
        成人：40
        其他：20
    */

    println("请输入月份")
    val month =StdIn.readInt()
    println("请输入年龄")
    val age = StdIn.readInt()
    val ticket =60
    if(month >= 4 && month <= 10){
      if(age >=18 && age<=60){
        println("你的票价是" + ticket)
      }else if(age <18){
        println("你的票价是" + ticket/2)
      }else{
        println("你的票价是" + ticket/3)
      }
    }else{
      if(age >= 18 && age <= 60){
        println("你的票价是" + 40)
      }else{
        println("你的票价是" + 20)
      }
    }
  }
}
