package com.lfw.unit6.exam

object DogCaseTest {
  /*
    小狗案例
    1) 编写一个Dog类，包含name(String)、age(Int)、weight(Double)属性
    2) 类中声明一个say方法，返回String类型，方法返回信息中包含所有属性值。
    3) 在另一个TestDog类中的main方法中，创建Dog对象，并访问say方法和所有属性，将调用结果打印输出。
   */
  def main(args: Array[String]): Unit = {
    val dog = new Dog
    dog.name = "tomcat"
    dog.age = 2
    dog.weight = 6
    println(dog.say())
  }
}

class Dog {
  var name = ""
  var age = 0
  var weight = 0.0

  def say(): String = {
    "小狗的信息如下：name=" + this.name + "\t age=" + this.age + " weight=" + this.weight
  }
}