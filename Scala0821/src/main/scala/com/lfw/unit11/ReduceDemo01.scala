package com.lfw.unit11

object ReduceDemo01 {
  def main(args: Array[String]): Unit = {
    //使用化简的方式来计算 list 集合的和
    val list = List(1, 20, 30, 4, 5)
    val res = list.reduceLeft(sum) //reduce/reduceLeft/reduceRight

    //执行的流程分析
    //步骤1 (1+20)
    //步骤2 (1+20) + 30
    //步骤3 ((1 + 20) + 30) + 4
    //步骤4 (((1 + 20) + 30) + 4) + 5 = 60

    println("res=" + res)
  }

  def sum(n1: Int, n2: Int): Int = {
    println("sum 被调用~~")
    n1 + n2
  }
}
