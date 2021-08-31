package com.lfw.unit10

object ListDemo03 {
  def main(args: Array[String]): Unit = {
    println("--------list 追加元素后的效果--------")
    //通过 :+ 和 +: 给 list 追加元素（本身的集合并没有变化）
    var list1 = List(1,2,3,"abc")
    //:+运算符表示在列表的最后增加数据
    val list2 = list1 :+ 4 //(1,2,3,"abc",4)
    println(list1)  //list1 没有变化（1,2,3,"abc")，说明list1还是不可变

    val list3 = 10 +: list1 //(10,1,2,3,"abc")
    println("list3=" +list3)
  }
}
