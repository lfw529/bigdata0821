package com.lfw.unit4.ifelse

object Exercise01 {
  def main(args: Array[String]): Unit = {
    //定义两个变量 Int，判断二者的和，是否既能被3又能被5整除，打印提示信息
    val num1 = 10
    val num2 = 5

    val sum = num1 +num2
    if(sum % 3 == 0 && sum % 5 ==0){
      println("能被3又能被5整除")
    }else{
      println("能被3又能被5整除不成立")
    }
  }
}
