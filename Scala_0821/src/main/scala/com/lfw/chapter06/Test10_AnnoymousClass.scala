package com.lfw.chapter06

object Test10_AnnoymousClass {
  def main(args: Array[String]): Unit = {
    //匿名子类的实现，重写实现抽象方法
    val person: Person10 = new Person10 {
      override var name: String = "alice"
      override def eat(): Unit = println("person eat")
    }
    println(person.name)  //alice
    person.eat()  //person eat
  }
}

// 定义抽象类
abstract class Person10 {
  var name: String
  def eat(): Unit
}
