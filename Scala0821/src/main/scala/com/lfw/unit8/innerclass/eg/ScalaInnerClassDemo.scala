package com.lfw.unit8.innerclass.eg

object ScalaInnerClassDemo {
  def main(args: Array[String]): Unit = {
    //测试1.创建了两个外部类的实例
    val outer1: ScalaOuterClass = new ScalaOuterClass()
    val outer2: ScalaOuterClass = new ScalaOuterClass()

    val inner1 = new outer1.ScalaInnerClass()
    val inner2 = new outer2.ScalaInnerClass()

    //测试一下使用 inner1 去调用 info()
    inner1.info()

    //创建静态内部类对象
    val staticInner = new ScalaOuterClass.ScalaStaticInnerClass()
  }
}

//外部类
//内部类如果想要访问外部类的属性，可以通过外部类对象访问。即：访问方式：外部类名.this.属性名
/*class ScalaOuterClass {
  //定义两个属性
  var name = "scoot"
  private var sal = 30000.9

  class ScalaInnerClass {
    def info() = {
      // 访问方式：外部类名.this.属性名
      // 怎么理解 ScalaOuterClass.this 就相当于是 ScalaOuterClass 这个外部类的一个实例, // 然后通过 ScalaOuterClass.this 实例对象去访问 name 属性
      // 只是这种写法比较特别，学习java的同学可能更容易理解 ScalaOuterClass.class 的写法.
      println("name = " + ScalaOuterClass.this.name + " age =" + ScalaOuterClass.this.sal)
    }
  }

}*/

//外部类
//内部类如果想要访问外部类的属性，也可以通过外部类别名访问（推荐）。即：访问方式：外部类别名.属性名
class ScalaOuterClass {
  myouter =>  //这里我们可以这样理解 外部类的别名 看作是外部类的一个实例
  class ScalaInnerClass{
    def info() ={
      //访问方式：外部类别名.属性名
      println("name~=" + myouter.name + "sal~=" +myouter.sal)
    }
  }
  //定义两个属性，将外部类的属性写在别名后面
  var name = "jack"
  private var sal = 800.9
}

object ScalaOuterClass{
  class ScalaStaticInnerClass{
    //成员内部类
  }
}

