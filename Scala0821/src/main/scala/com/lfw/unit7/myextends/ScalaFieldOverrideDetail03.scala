package com.lfw.unit7.myextends

object ScalaFieldOverrideDetail03 {
  def main(args: Array[String]): Unit = {
    val T:Sub_A03 = new Sub_A03
    println(T.name)
    println("hello~")
  }
}

//在 A03 中，有一个抽象字段（属性）
//1.抽象的字段（属性）：就是没有初始化的字段（属性）
//2.当一个类含有抽象属性时，则该类需要标记为 abstract
//3.对于抽象的属性，在底层不会生成对应的属性声明，而是生成两个对应的抽象方法（name() / name_$eq()）
abstract class A03 {
  var name: String //抽象属性
  var age: Int = 10
}

class Sub_A03 extends A03 {
  //说明
  //1.如果我们在子类中去重写父类的抽象属性，本质是实现了抽象方法
  //2.因此这里我们可以写 override ，也可以不写
  override var name: String = ""
}