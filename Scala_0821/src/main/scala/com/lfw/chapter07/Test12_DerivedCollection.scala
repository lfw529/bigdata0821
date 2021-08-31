package com.lfw.chapter07

object Test12_DerivedCollection {
  def main(args: Array[String]): Unit = {
    val list1 = List(1, 3, 5, 7, 2, 89)
    val list2 = List(3, 7, 2, 45, 4, 8, 19)

    //1.获取集合的头
    println(list1.head) //1
    println("-----------------------")
    //2.获取集合的尾（不是头的就是尾）
    println(list1.tail) // List(3, 5, 7, 2, 89)
    println("-----------------------")
    //3.集合最后一个数据
    println(list2.last)  //19
    println("-----------------------")
    //4.集合初始数据（不包含最后一个）
    println(list2.init)   //List(3, 7, 2, 45, 4, 8)
    println("-----------------------")
    //5.反转
    println(list1.reverse)  //List(89, 2, 7, 5, 3, 1)
    println("-----------------------")
    //6.取前（后）n个元素
    println(list1.take(3))  //前面3个 List(1, 3, 5)
    println(list1.takeRight(4))  //后面4个 List(5, 7, 2, 89)
    println("-----------------------")
    //7.去掉前（后）n个元素
    println(list1.drop(3))  //List(7, 2, 89) 丢掉前面3个
    println(list1.dropRight(4)) //List(1, 3)  丢掉后面4个
    println("=========================")
    //8.并集
    //方式1：union求并集
    val union = list1.union(list2)
    println("union: " + union)  //union: List(1, 3, 5, 7, 2, 89, 3, 7, 2, 45, 4, 8, 19)
    //方式2：符号操作： ::: 追加
    println(list1 ::: list2)  //List(1, 3, 5, 7, 2, 89, 3, 7, 2, 45, 4, 8, 19)
    println("=========================")
    // 如果是set做并集，会去重
    val set1 = Set(1, 3, 5, 7, 2, 89)
    val set2 = Set(3, 7, 2, 45, 4, 8, 19)
    //union()方法，无序
    val union2 = set1.union(set2)
    println("union2: " + union2)  //union2: Set(5, 89, 1, 2, 45, 7, 3, 8, 19, 4)
    //符号操作： ++ 无序合并，去重
    println(set1 ++ set2)  //Set(5, 89, 1, 2, 45, 7, 3, 8, 19, 4)
    println("-----------------------")
    //9.交集
    val intersection = list1.intersect(list2)
    println("intersection: " + intersection) //intersection: List(3, 7, 2)
    println("-----------------------")
    //10.差集
    val diff1 = list1.diff(list2)
    val diff2 = list2.diff(list1)
    println("diff1: " + diff1)  //diff1: List(1, 5, 89) 属于list1 不属于list2
    println("diff2: " + diff2)  //diff2: List(45, 4, 8, 19)  属于list2 不属于list1
    println("-----------------------")
    //11.拉链
    //list1 在前，list2 在后
    println("zip: " + list1.zip(list2))  //zip: List((1,3), (3,7), (5,2), (7,45), (2,4), (89,8))
    //list2 在前，list1 在后
    println("zip: " + list2.zip(list1))
    println("-----------------------")
    //12.滑窗
    for (elem <- list1.sliding(3)) {  //以3个元素为窗口，开窗
      println(elem)
//      List(1, 3, 5)
//      List(3, 5, 7)
//      List(5, 7, 2)
//      List(7, 2, 89)
    }
    println("-----------------------")
    for (elem <- list2.sliding(4, 2))  //以4个元素为窗口，开窗，步长为2
      println(elem)
//    List(3, 7, 2, 45)
//    List(2, 45, 4, 8)
//    List(4, 8, 19)
    println("-----------------------")
    for (elem <- list2.sliding(3, 3))  //以3个元素为窗口，开窗，步长为3
      println(elem)
//    List(3, 7, 2)
//    List(45, 4, 8)
//    List(19)
  }
}

