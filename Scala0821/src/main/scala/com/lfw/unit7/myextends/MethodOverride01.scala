package com.lfw.unit7.myextends

object MethodOverride01 {
  def main(args: Array[String]): Unit = {
    val emp = new Emp100
    emp.printName()
  }
}

//Person类
class Person100 {
  var name: String = "tom"

  def printName() { // 输出名字
    println("Person printName()" + name)
  }

  def sayHi(): Unit = {
    println("sayHi...")
  }
}

//这里我们继承 Person
class Emp100 extends Person100 {
  //这里需要显示的调用 override
  override def printName(): Unit = {
    println("Emp printName()" + name)   //Emp printName()tom
    //在子类中需要去调用父类的方法，使用 super
    super.printName()   //Person printName()tom
    sayHi()    //sayHi...
  }
}