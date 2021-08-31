package com.lfw.unit4.ifelse

object Exercise02 {
  def main(args: Array[String]): Unit = {
    //定义一个变量保存年份
    val year = 2018
    if((year % 4 == 0 && year % 100 !=0)||(year % 400 ==0)){
      println(s"${year} 是闰年...")
    }else{
      println(s"${year} 不是闰年")
    }
  }
}
