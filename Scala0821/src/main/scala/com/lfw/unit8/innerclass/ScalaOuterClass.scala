package com.lfw.unit8.innerclass

object ScalaOuterClass{  //伴生对象
  class ScalaStaticInnerClass{
    //静态内部类
  }
}

class ScalaOuterClass{
  class ScalaInnerClass{
    //成员内部类
  }
}
object test{
  def main(args: Array[String]): Unit = {
    val outer1:ScalaOuterClass = new ScalaOuterClass()
    val outer2:ScalaOuterClass = new ScalaOuterClass()

    //Scala创建内部类的方式和Java不一样，将new关键字放置在前，使用 对象.内部类 的方式创建
    val inner1 = new outer1.ScalaInnerClass()
    val inner2 = new outer2.ScalaInnerClass()
    //创建静态内部类对象
    val staticInner = new ScalaOuterClass.ScalaStaticInnerClass()
    println(staticInner) //com.lfw.unit8.innerclass.ScalaOuterClass$ScalaStaticInnerClass@5fe5c6f
  }
}
