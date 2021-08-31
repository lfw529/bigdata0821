package com.lfw.unit6.exam

object Test06 {
  /*
    同样是解决前一个练习的问题，请用StringOps的foreach方式解决。
   */
  def main(args: Array[String]): Unit = {
    var res = 1L

    "Hello".foreach(res *= _.toLong)
    println("res=" + res)
  }
}
