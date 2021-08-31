package com.lfw.unit8.mixin

object MixInDemo02 {
  def main(args: Array[String]): Unit = {
    println("xxx")
    val mySQL5 = new MySQL5 with DB5 with File5

    //1.将数据保存到文件中..
    //2.将数据保存到数据库中..      //动态混入调用
    mySQL5.insert(777)

    //下面的混入方法时错误的(由抽象方法没实现)
//    val mySQL5_ = new MySQL5 with File5
//    mySQL5_.insert(77)

    //四个案例
    var mysql22 = new MySQL5 with DB5 //ok
    mysql22.insert(100)
//    var mysql23 = new MySQL5 with File5 //error
//    mysql23.insert(100)
//    var mysql24 = new MySQL5 with File5 with DB5// error
//    mysql24.insert(100)
    var mysql25 = new MySQL5 with DB5 with File5// ok
    mysql25.insert(100)
  }
}

trait Operate5 {
  def insert(id: Int)
}

trait File5 extends Operate5 {
  //说明
  //1.如果我们在子特质中重写/实现了一个父特质的抽象方法，但是同时调用super
  //2.这时我们的方法不是完全实现，因此需要声明为 abstract override
  //3.这时super.insert(id) 的调用就和动态混入顺序有密切关系
  abstract override def insert(id: Int): Unit = {
    println("将数据保存到文件中..")
    super.insert(id)
  }
}

trait DB5 extends Operate5 { //继承了Operate5,并实现了Operate5 的insert方法
  def insert(id: Int): Unit = {
    println("将数据保存到数据库中..")
  }
}

class MySQL5()
