package com.lfw.unit8.mixin

object MixInDemo01 {
  def main(args: Array[String]): Unit = {
    //在不修改类的定义基础，让他们可以使用trait方法
    val oracleDB = new OracleDB with Operate3 //ocp 原则
    oracleDB.insert(100) //100

    val mySQL = new MySQL3 with Operate3
    mySQL.insert(200)  //200

    //如果一个抽象类有抽象方法，如何动态混入特质
    val mySQL_ = new MySQL3_ with Operate3 {
      override def say(): Unit = {
        println("say")
      }
    }
    mySQL_.insert(999)  //999
    mySQL_.say()  //say
  }
}

trait Operate3 {
  def insert(id: Int): Unit = {
    println("插入数据= " + id)
  }
}

class OracleDB{

}

abstract class MySQL3 {

}

abstract class MySQL3_ {
  def say()
}