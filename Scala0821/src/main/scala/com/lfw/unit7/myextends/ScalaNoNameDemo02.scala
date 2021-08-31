package com.lfw.unit7.myextends

object ScalaNoNameDemo02 {
  def main(args: Array[String]): Unit = {
    val monster = new Monster{
      override var name: String = _

      override def cry(): Unit = {
        println("妖怪嗷嗷叫...")
      }
    }
    monster.cry()
  }
}

abstract class Monster{
  var name:String
  def cry()
}