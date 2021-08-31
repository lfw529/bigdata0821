package com.lfw.unit6.constructor

object ConDemo01 {
  def main(args: Array[String]): Unit = {
    val p1 = new Person("jack", 20)
    println(p1)

    val a = new A
    val a2 = new A()

    //下面这句话就会调用 def this(name:String)
    val p2 = new Person("tom")
    println(p2)

    /*  -----------------
      ok~~~~
        age=30
      name=jack	 age30
        -----------------
      ok~~~~
        age=20
      name=tom	 age20
  */
  }
}

class Person(inName: String, inAge: Int) {
  var name: String = inName
  var age: Int = inAge
  age += 10
  println("-----------------")

  //重写了 toString,标语输出对象的信息
  override def toString: String = {
    "name=" + this.name + "\t age" + this.age
  }

  println("ok~~~~")
  println("age=" + age)

  def this(name: String) {
    //辅助构造器，必须在第一行显示调用主构造器（可以是直接，也可以是间接）
    this("Jack", 10)
    //this
    this.name = name //重新赋值
  }
}

class A() {

}