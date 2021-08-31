package com.lfw.unit2.dataconvert

object Demo02 {
  def main(args: Array[String]): Unit = {
    val num1: Int = 10 * 3.5.toInt + 6 * 1.5.toInt //36
    val num2: Int = (10 * 3.5 + 6 * 1.5).toInt //44

    println(num1 + " " + num2)

    val char1: Char = 1
    val num3 = 1
    //    var char2: Char = num3  //报错，char 无法保存 int 的变量值。
  }
}
