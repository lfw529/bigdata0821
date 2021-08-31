package com.lfw.unit3.arithoper

object Demo03 {
  def main(args: Array[String]): Unit = {
    var a = true
    var b = false
    println("a && b = " + (a && b)) // false
    println("a || b = " + (a || b)) // true
    println("!(a && b) = " + !(a && b)) // true
  }
}
