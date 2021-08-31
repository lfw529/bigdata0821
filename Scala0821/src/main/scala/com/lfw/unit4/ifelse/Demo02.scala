package com.lfw.unit4.ifelse

import scala.io.StdIn

object Demo02 {
  /*
    岳小鹏参加 scala 考试，他和父亲岳不群达成承诺：
    如果：
    成绩为100分时，奖励一辆 BMW；
    成绩为 (80，99] 时，奖励一台 iphone7plus；
    当成绩为 [60,80] 时，奖励一个 iPad；
    其它时，什么奖励也没有
    说明：成绩在控制台输入
   */
  def main(args: Array[String]): Unit = {
    println("请输入成绩：")
    val score = StdIn.readDouble()
    if (score == 100) {
      println("成绩为100分时，奖励一辆 BMW；")
    } else if (score > 80 && score <= 99) {
      println("成绩为 (80，99] 时，奖励一台 iphone7plus；")
    } else if (score > 60 && score <= 80) {
      println("成绩为 [60,80] 时，奖励一个 iPad；")
    } else {
      println("没有任何奖励")
    }
  }
}
