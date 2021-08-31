package com.lfw.unit6.exam

object Test07 {
  def main(args: Array[String]): Unit = {
    def product(s: String): Long = {
      var r: Long = 1L
      for (i <- s) {
        r *= i.toLong
      }
      r   //此处的 r 为返回值
    }
  }
}
