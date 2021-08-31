package com.lfw.unit8.exam

object Test04 {
  def main(args: Array[String]): Unit = {
    //type 可以用于给类型取别名
    type MyInt = Int //给Int 去了别名
    val num1: MyInt = 888
    println("num1=" + num1)
    println("-----------------------")
    println(Suits)
    println(Suits.isRed(Suits.Heart))  //true
    println(Suits.isRed(Suits.Spade))  //false

  }
}

object Suits extends Enumeration{
  type Suits = Value //给 Value 类型取别名
  val Spade = Value("♠") //创建一个Value对象
  val Club = Value("♣")
  val Heart = Value("♥")
  val Diamond = Value("♦")

  override def toString(): String = {  //重写 toString 方法
    Suits.values.mkString(",")
  }

  def isRed(suit: Suits) = {
//    if(suit == Heart || suit == Diamond){
//      true
//    }else {
//      false
//    }
    suit == Heart || suit == Diamond
  }
}