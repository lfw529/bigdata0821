package com.lfw.unit11

object Exercise01 {
  def main(args: Array[String]): Unit = {
    val names = List("Alice","Bob","Nick")
    val names2 = names.map(upper)
    println("names=" +names2)  //names=List(ALICE, BOB, NICK)
  }

  def upper(s:String):String = {
    s.toUpperCase
  }
}
