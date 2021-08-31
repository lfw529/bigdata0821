package com.lfw.unit6.constructor

object ConDemo03 {
  def main(args: Array[String]): Unit = {
    val worker = new Worker("smith")
    worker.name //不能访问 inName

    println("-------------------")
    val worker2 = new Worker2("smith2")
    worker2.inName //可以访问 inName

    println("-------------------")
    val worker3 = new Worker3("jack")
    worker3.inName = "mary"
    println(worker3.inName)
  }
}

//1.如果 主构造器是 Worker(inName:String),那么，inName 就是一个局部变量
class Worker(inName: String) {
  var name = inName
}

//2.如果主构造器 Worker2(val inName:String),那么 inName 就是 Worker2 的一个private的只读属性
class Worker2(val inName: String) {
  var name = inName;
}

//3.如果主构造器 Worker3(var inName:String),那么 inName 就是 Worker3 的一个private的可以读写属性
class Worker3(var inName: String) {
  var name = inName
}