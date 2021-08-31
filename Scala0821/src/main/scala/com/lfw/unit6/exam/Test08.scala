package com.lfw.unit6.exam

object Test08 {
  def main(args: Array[String]): Unit = {
    println("res=" + product2("Hello"))

    println("Hello".take(1))
    println("Hello".drop(1))
  }

  def product2(s:String):Long = {
    //如果字符串长度为1，就直接返回 s.charAt(0).toLong
    if(s.length == 1) return s.charAt(0).toLong
    //1.如果不是=1，s.take(1)就是返回 s 的第一个字符(String)
    //s.drop(1) 返回的 s 字符串 出去第一个字符的后面所有的字符(String)
    else s.take(1).charAt(0).toLong * product2(s.drop(1))
  }
}
