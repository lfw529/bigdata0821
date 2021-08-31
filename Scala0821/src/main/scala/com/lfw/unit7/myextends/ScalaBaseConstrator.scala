package com.lfw.unit7.myextends

object ScalaBaseConstructor {
  def main(args: Array[String]): Unit = {
    //分析一下它的执行流程
    //1.因为scala遵守先构建父类部分 extends Person 700()
    //2.Person....
    val emp = new Emp700()
    /*--------------------------------运行结果
      Person...
      默认的名字
      Emp....
     */
  }
}

//父类 Person
class Person700(pName: String) {
  var name = pName
  println("Person...")

  def this() {
    this("默认的名字")
    println("默认的名字")
  }
}

//子类 Emp 继承 Person
class Emp700() extends Person700 {
  println("Emp....")

  //辅助构造器
  def this(name: String) {
    this //必须调用主构造器
    this.name = name
    println("Emp 辅助构造器~")
  }
}