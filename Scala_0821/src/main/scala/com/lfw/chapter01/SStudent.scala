package com.lfw.chapter01

class SStudent(name: String, var age: Int) {
  def printInfo(): Unit = {
    println(name + " " + age + " " + SStudent.school)
  }
}

//引入伴生对象，scala没有static关键字
object SStudent {
  val school: String = "University"

  def main(args: Array[String]): Unit = {
    val alice = new SStudent("alice", 20)
    val bob = new SStudent("bob", 23)

    alice.printInfo()
    bob.printInfo()
  }
}

