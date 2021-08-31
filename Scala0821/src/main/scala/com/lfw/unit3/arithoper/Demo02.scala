package com.lfw.unit3.arithoper

object Demo02 {
  def main(args: Array[String]): Unit = {
    var a = 9
    var b = 8
    println(a > b) // true
    println(a >= b) // true
    println(a <= b) // false
    println(a < b) // false
    println(a == b) // false
    println(a != b) // true
    var flag: Boolean = a > b // true
    println(flag)
  }
}
