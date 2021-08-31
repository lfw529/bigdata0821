package com.lfw.unit2.datatype

object UnitNullNothingDemo {
  def main(args: Array[String]): Unit = {
    val res = sayHello()
    println("res=" + res)   //输出：res=()

    //Null 类只有一个实例对象，null，类似于 Java 中的 null 引用。null 可以赋值给任意引用类型（AnyRef），
    //但是不能赋值给值类型（AnyVal：比如 Int，Float，Char，Boolean，Long，Double，Byte，Short）
    val dog: Dog = null
    //错误
    //val char1: Char = null  //运行错误
    println("ok~~~")
  }

  //Unit 等价于 java 的 void,只有一个实例值()
  def sayHello(): Unit = {

  }
}

class Dog {

}