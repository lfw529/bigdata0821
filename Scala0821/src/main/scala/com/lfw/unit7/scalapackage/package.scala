package com.lfw.unit7

//说明
//1.在包中直接写方法，或者定义变量，会报错==>使用包对象来解决
//2.package object scala 表示创建一个包对象 scala，他是 com.lfw.scala这个包对应的包对象
//3.每一个包都可以有一个包对象
//4.包对象的名字需要和子包一样
//5.在包对象中可以定义变量，方法
//6.在包对象中定义的变量和方法，就可以在对应的包中使用
//7.在底层这个包对象会生成两个类 package.class 和 package$.class
package object scala {
  var name = "king"

  def sayHiv(): Unit = {
    println("package object scala sayHI~")
  }
}

package scala {

  class User {
    def testUser(): Unit = {
      println("name=" + name)
      sayHiv()
    }
  }

  object Test700 { //表示在 com.lfw.scala 创建 object Test
    def main(args: Array[String]): Unit = {
      println("name=" + name)
      name = "yy"
      sayHiv()
    }
  }
}
