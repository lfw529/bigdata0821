package com.lfw.unit10

import scala.collection.mutable

object SetDemo02 {
  def main(args: Array[String]): Unit = {
    val mutableSet = mutable.Set(1, 2, 3)
    mutableSet.add(4) //方式1
    mutableSet += 6 //方式2
    mutableSet.+=(5) //方式3
    println(mutableSet) //Set(1, 5, 2, 6, 3, 4)
    //如果添加的对象已经存在，不会重复添加，也不会报错
    println("--------------------")
    val set02 = mutable.Set(1, 2, 4, "abc")
    set02 -= 2 //操作符形式
    set02.remove("abc") //方法的形式，scala的Set可以直接删除值
    println(set02)  //Set(1, 4)
    //说明：如果删除的对象不存在，则不生效，也不会报错
    println("---------------------")
    val set03 = mutable.Set(1, 2, 4, "abc")
    for (x <- set03) {
      print(x + " ") //1 2 abc 4
    }
  }
}
