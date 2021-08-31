package com.lfw.unit8.mytrait

object TraitDemo03 {
  def main(args: Array[String]): Unit = {
    println("~~~~~")
    //创建 sheep
    val sheep = new Sheep
    sheep.sayHi()
    sheep.sayHello()
  }
}

//当一个 trait 有抽象方法和非抽象方法时
//1.一个 trait 在底层对应两个 Trait03.class 接口
//2.还对应 Trait03$class.class Trait03$class 抽象类

trait Trait03 {
  //抽象方法
  def sayHi()

  //实现普通方法
  def sayHello(): Unit = {
    println("say Hello~~")
  }
}

//当trait有接口和抽象类
//1.class Sheep extends Trait03 在底层 对应
//2.class Sheep implements Trait03
//3.当在 Sheep 类中要使用 Trait03 的实现的方法，就通过 Trait03$class
class Sheep extends Trait03 {
  override def sayHi(): Unit = {
    println("小羊 say Hi~~")
  }
}
