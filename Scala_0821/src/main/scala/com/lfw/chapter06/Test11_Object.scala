package com.lfw.chapter06

object Test11_Object {
  def main(args: Array[String]): Unit = {
    //    val student = new Student11("alice", 18)
    //    student.printInfo()
    val student1 = Student11.newStudent("alice", 18)
    student1.printInfo()
    //不用再new对象了，直接调用
    val student2 = Student11.apply("bob", 19)
    student2.printInfo() //student: name = bob, age = 19, school = lfw
    val student3 = Student11("bob", 19)
    student3.printInfo()
  }
}

// 定义类
class Student11 private(val name: String, val age: Int){
  def printInfo(){
    println(s"student: name = ${name}, age = $age, school = ${Student11.school}")
  }
}

// 伴生对象
object Student11{
  val school: String = "lfw"
  // 定义一个类的对象实例的创建方法 [自己定义]
  def newStudent(name: String, age: Int): Student11 = new Student11(name, age)
  //等价于上面的scala提供的构造方法
  def apply(name: String, age: Int): Student11 = new Student11(name, age)
}
