package com.lfw.unit8.selftype

class SelfTypeDemo {
  def main(args: Array[String]): Unit = {

  }
}

//Logger 就是自身类型特质，当这里做了自身类型后，那么
//trait Logger extends Exception，要求混入特质的类也是 Exception 子类
trait Logger {
  //明确告诉编译器，我就是 Exception ，如果没有这句话，下面的getMessage不能调用
  this:Exception=>
  def log():Unit={
    //既然我就是 Exception ，那么就可以调用其中的方法
    println(getMessage)
  }
}

//class Console extends Logger{}  //错误
class Console extends Exception with Logger {}//正确