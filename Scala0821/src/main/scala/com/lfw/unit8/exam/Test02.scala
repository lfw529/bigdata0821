package com.lfw.unit8.exam

object Test02 {
  def main(args: Array[String]): Unit = {
    val point = Point(3, 4) //触发apply
    println(point)
  }
}

object Point {
  /*
    定义一个 Point 类和一个伴生对象,使得我们可以不用 new 而直接用 Point(3,4)来构造 Point 实例 apply 方法的使用
   */
  def apply(x: Int = 0, y: Int = 0) = new Point(x, y)
}

class Point(x: Int = 0, y: Int = 0) { //Point类
  var x1 = x //属性
  var y1 = y //属性
}
