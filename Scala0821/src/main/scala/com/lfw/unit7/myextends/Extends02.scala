package com.lfw.unit7.myextends

//说明
//1.在 scala 中，子类继承了父类的所有属性
//2.但是 private 的属性和方法无法访问
//总结：
//1. 没有修饰符，默认为 public,可在任意地方使用
//2. protected,底层解析为public,只能在本类和子类中使用，同包都不行
//3，private,底层解析为private,只能在本类和伴生对象中使用
object Extends02 {
  def main(args: Array[String]): Unit = {
    val sub = new Sub()
    sub.sayOk()
    //sub.test200()  //报错
  }
}

//父类
class Base {
  var n1: Int = 1  //public n1(),public n1_$eq()
  protected var n2: Int = 2
  private var n3: Int = 3  //public n3(),public n3_$eq()

  def test100(): Unit = {  //默认 public test100()
    println("base 100")
  }

  protected def test200(): Unit = {//public ,反编译解析为 public
    println("base 200")
  }

  private def test300(): Unit = { //private
    println("base 300")
  }
}

class Sub extends Base {
  def sayOk(): Unit = {
    this.n1 = 20  //这里访问的本质 this.n1_$eq()
    this.n2 = 40
    println("范围" + this.n1 + this.n2)

    test100()//
    test200()//
  }
}