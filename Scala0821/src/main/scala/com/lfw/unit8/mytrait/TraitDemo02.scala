package com.lfw.unit8.mytrait

object TraitDemo02 {
  def main(args: Array[String]): Unit = {
    val c = new C()
    val f = new F()
    c.getConnect() //连接 mysql 数据库...
    f.getConnect() //连接 oracle 数据库...
  }
}

//按照要求定义一个 trait
trait Trait01 {
  //定义一个规范
  def getConnect()
}

//先将六个类的关系写出
class A {}

class B extends A {}

class C extends A with Trait01 {
  override def getConnect(): Unit = {
    println("连接 mysql 数据库....")
  }
}

class D {}

class E extends D {}

class F extends D with Trait01 {
  override def getConnect(): Unit = {
    println("连接 Oracle 数据库....")
  }
}
