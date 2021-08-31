package com.lfw.unit2

object VarDemo02 {
  def main(args: Array[String]): Unit = {
    //类型推导
    var num = 10 //这时 num 就是 Int
    //方式1，可以利用 idea 的提示来证明，给出提示
    //方式2，使用 isInstanceOf[Int] 判断
    println(num.isInstanceOf[Int]);

    //类型确定后，就不能修改，说明 Scala 是强数据类型语言
    //num = 2.3 , 报错

    //3.在声明/定义一个变量时，可以使用 var 或者 val 来修饰，var 修饰的变量可改变，val 修饰的变量不可改变
    var age = 10 //即 age 是可以改变的 (只能是同类型转)
    age = 30 //ok

    val num2 = 30
    //num2 = 40  //报错 val 修饰的变量是不可改变的

    //scala 设计者为什么这么设计 var 和 val
    //(1) 在实际编程中，我们更多的需求是获取/创建一个对象后，读取该对象的属性，
    //或者是修改对象的属性值，但是我们很少去改变这个对象本身
    // dog = new Dog();   dog.age = 10;   dog = new Dog();
    //这时，我们就可以使用 val
    //(2) 因为 val 没有线程安全问题，因此效率高，scala 的设计者推荐使用 val
    //(3) 如果对象需要改变，则使用 var

    val dog = new Dog
    //dog = new Dog //Reassignment to val
    dog.age = 90  //ok
    dog.name = "小花"  //ok   //内部属性可以发生更改
  }
}

class Dog {
  //声明一个 age 属性，给了一个默认值
  var age: Int = 0 //
  //声明名字
  var name: String = "" //
}