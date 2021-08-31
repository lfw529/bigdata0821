package com.lfw.unit7.myextends

object AbstractDemo02 {
  def main(args: Array[String]): Unit = {
    //默认情况下，一个抽象类是不能实例化的，但是在实例化时，动态的实现类抽象类的所有抽象方法，也是可以的
    val animal = new Animal02 {
      override def sayHello(): Unit = {
        println("say Hello~~~")
      }
    }
    animal.sayHello()
  }
}

abstract class Animal02 {
  def sayHello()

  def sayBye(): Unit = {
    println("xxx")
  }
}

abstract class Animal03 {
  def sayHello()

  var food: String
}

class Dog extends Animal03 {
  override var food: String = _

  override def sayHello(): Unit = {
    println("小狗汪汪叫！")
  }
}