package com.lfw.chapter07

object Test04_List {
  def main(args: Array[String]): Unit = {
    //1.创建一个List
    val list1 = List(23, 65, 87)
    println(list1)  //List(23, 65, 87)

    //2.访问和遍历元素
    println(list1(1))    //65   访问第一个元素
//    list1(1) = 12
    list1.foreach(println)  //23 65 87

    //3.添加元素
    val list2 = 10 +: list1  //在头部添加
    val list3 = list1 :+ 23  //在尾部添加
    println(list1)  //List(23, 65, 87)
    println(list2)  //List(10, 23, 65, 87)
    println(list3)  //List(23, 65, 87, 23)
    println("==================")
    // .::在头部添加元素
    val list4 = list2.::(51)
    println(list4)  //List(51, 10, 23, 65, 87)
    //Nil 是空集合，向空集合中追加元素13
    val list5 = Nil.::(13)
    println(list5)
    //多 :: 的添加顺序就是从头按顺序添加
    val list6 = 73 :: 32 :: Nil
    val list7 = 17 :: 28 :: 59 :: 16 :: Nil
    println(list6)  //List(73, 32)
    println(list7)  //List(17, 28, 59, 16)
    println("--------------------------")
    // 4. 合并列表
    //不会拆解列表结构的合并
    val list8 = list6 :: list7
    println(list8)  //List(List(73, 32), 17, 28, 59, 16)
    //拆解列表结构
    val list9 = list6 ::: list7
    println(list9)  //List(73, 32, 17, 28, 59, 16)
    //其内部本质也是调用 ::: ，结果与上面一样
    val list10 = list6 ++ list7
    println(list10)  //List(73, 32, 17, 28, 59, 16)
  }
}
