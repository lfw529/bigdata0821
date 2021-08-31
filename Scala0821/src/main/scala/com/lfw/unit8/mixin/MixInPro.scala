package com.lfw.unit8.mixin

object MixInPro {
  def main(args: Array[String]): Unit = {
    val mySQL = new MySQL6 with DB6 {
      override var sal: Int = _  //类型须一致
    }
  }
}

trait DB6 {
  var sal: Int //抽象字段
  var opertype: String = "insert"

  def insert(): Unit = {
  }
}

class MySQL6 {}
