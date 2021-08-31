package com.lfw.chapter06

object Test04_Access {
  def main(args: Array[String]): Unit = {
    // 创建对象
    val person: Person = new Person()
    //    person.idCard    // error  私有属性不能外部访问
    //    person.name    // error    同类、子类可以访问，同包无法访问
    println(person.age)
    println(person.sex)
    person.printInfo()
    var worker: Worker = new Worker()
    //    worker = new Worker()
    //    worker.age = 23
    worker.printInfo()
  }
}

// 定义一个子类
class Worker extends Person {
  override def printInfo(): Unit = {
    //    println(idCard)    // error  只在类的内部和伴生对象中可用
    name = "bob"
    age = 25
    sex = "male"
    println(s"Worker: $name $sex $age")
  }
}
