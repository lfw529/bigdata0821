package com.lfw.unit2.datatype

object TypeDemo03 {
  def main(args: Array[String]): Unit = {
    println("long 的最大值" + Long.MaxValue + "~" + Long.MinValue)

    var i = 10 //i Int
    var j = 10l //j Long
    var e = 9223372036854775807l //报错，Integer literal is out of range for type Int，所以需要手动加上l

    //2.2345678912f ,2.2345678912
    var num1: Float = 2.2345678912f
    var num2: Double = 2.2345678912
    println("num1=" + num1 + "\t" + "num2=" + num2)
  }
}
