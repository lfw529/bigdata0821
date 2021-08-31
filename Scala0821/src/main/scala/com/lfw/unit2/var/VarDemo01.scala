package com.lfw.unit2

object VarDemo01 {
  def main(args: Array[String]): Unit = {
    var age: Int = 10
    var sal: Double = 10.9
    var name: String = "tom"
    var isPass: Boolean = true
    //在 scala中，小数默认为 Double，证书默认为Int
    var score: Float = 70.9f
    printf(s"${age} ${isPass}")
  }
}
