package com.lfw.unit10

object ArrayDemo02 {
  def main(args: Array[String]): Unit = {
    //说明
    //1.使用的是 object Array 的 apply
    //2.直接初始化数组，这时因为你给了整数和 ""，这个数组的泛型就是 Any
    //3.遍历方式一样
    var arr02 = Array(1, 3, "xx")
    arr02(1) = "xx"
    for (i <- arr02) {
      println(i)
    }
    println("-------------------")
    //可以使用我们传统的方式遍历，使用效标的方式遍历
    for (index <- 0 until arr02.length) {
      printf("arr02[%d]=%s", index, arr02(index) + "\t")
    }
  }
}
