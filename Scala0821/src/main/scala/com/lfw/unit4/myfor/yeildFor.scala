package com.lfw.unit4.myfor

object yeildFor {
  def main(args: Array[String]): Unit = {
    //说明 val res = for(i <- 1 to 10) yield i 含义
    //1.对 1 到 10 进行遍历
    //2.yield i 将每次循环得到 i 放入到集合 Vector 中，并返回给 res
    //3.i 这里是一个代码块，这就意味着我们可以对 i 进行处理
    //4.下面的这个方式，就体现出 scala 一个重要的语法特点，就是将一个集合中各个数据进行处理，并返回给新的集合

    val res = for (i <- 1 to 10) yield {
      if (i % 2 == 0) {
        i
      } else {
        "不是偶数"
      }
    }
    println(res)  //Vector(不是偶数, 2, 不是偶数, 4, 不是偶数, 6, 不是偶数, 8, 不是偶数, 10)
  }
}
