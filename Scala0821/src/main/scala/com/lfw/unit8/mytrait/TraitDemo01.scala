package com.lfw.unit8.mytrait

object TraitDemo01 {
  def main(args: Array[String]): Unit = {

  }
}

//trait Serializable extends Any with java.io.Serializable
//在 scala 中，java的接口都可以当做 trait 来使用（如上面的语法）
object T1 extends Serializable{

}
object T2 extends Serializable{

}
