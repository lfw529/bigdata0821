package com.lfw.unit12.casepak

object CaseClassDemo01 {
  def main(args: Array[String]): Unit = {
      println("hello~~")
  }
}

abstract class Amount
case class Dollar(value: Double) extends Amount    //样例类
case class Currency(value: Double, unit: String) extends Amount //样例类
case object NoAmount extends Amount  //样例类
//类型（对象）=序列化（serializable）==>字符串（1.你可以保存到文件中 2.反序列化 3.网络传输）

