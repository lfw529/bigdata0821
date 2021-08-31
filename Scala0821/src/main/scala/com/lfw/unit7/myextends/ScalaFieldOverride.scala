package com.lfw.unit7.myextends

object ScalaFieldOverride {
  def main(args: Array[String]): Unit = {
    val obj1: AAA = new BBB
    val obj2: BBB = new BBB
    //obj1.age => obj1.age() //动态绑定机制
    //obj2.age => obj2.age()
    println("obj1.age=" + obj1.age + "\t obj2.age=" + obj2.age)
  }
}
//如果val age改成 var 报错
class AAA {
  val age: Int = 10 //会生成 public age()
}

class BBB extends AAA {
  override val age: Int = 20 //会生成 public age()
}
