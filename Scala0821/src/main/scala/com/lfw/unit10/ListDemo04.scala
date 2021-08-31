package com.lfw.unit10

object ListDemo04 {
  def main(args: Array[String]): Unit = {
    //:: 符号的使用
    val list4 = List(1, 2, 3, "abc")
    //说明 val list5 = 4::5::6::list4::Nil 步骤
    //1.List()
    //2.List(List(1,2,3,"abc"))
    //3.List(6,List(1,2,3,"abc"))
    //4.List(5,6,List(1,2,3,"abc"))
    //5.List(4,5,6,List(1,2,3,"abc"))
    val list5 = 4 :: 5 :: 6 :: list4 :: Nil
    println("list5=" + list5)

    //说明 val list6 = 4::5::6::list4:::Nil 步骤
    //1.List()
    //2.List(1,2,3,"abc")
    //3.List(6,1,2,3,"abc")
    //4.List(5,6,1,2,3,"abc")
    //5.List(4,5,6,1,2,3,"abc")
    val list6 = 4 :: 5 :: 6 :: list4 ::: Nil
    println("list6=" + list6)
  }
}
