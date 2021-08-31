package com.lfw.unit7.scalapackage

object TestTiger {
  def main(args: Array[String]): Unit = {
    //使用 xh 的 Tiger
    val tiger1 = new com.lfw.unit7.scalapackage.xh.Tiger
    //使用 xm 的 Tiger
    val tiger2 = new com.lfw.unit7.scalapackage.xm.Tiger
    println(tiger1)
    println(tiger2)
  }
}
