package com.lfw.unit3.notice

object Demo01 {
  def main(args: Array[String]): Unit = {

    val num = if (5 > 4) 5 else 4
    //    val num2 = 5 > 4 ? 5: 4  //报错，没有三元运算符

    //求两个数最大值
    val n1 = 4
    val n2 = 8
    var res = if (n1 > n2) n1 else n2
    println("res= " + res)

    //求三个数最大值
    val n3 = 11
    res = if (res > n3) res else n3
    println("res= " + res)
  }
}
