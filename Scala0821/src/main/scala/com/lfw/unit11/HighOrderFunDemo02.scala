package com.lfw.unit11

object HighOrderFunDemo02 {
  def main(args: Array[String]): Unit = {
    test2(sayOk)
  }

  //说明 test2 是一个高阶函数，可以接收一个没有输入，返回为 Unit 的函数
  def test2(f: () => Unit) = {
    f()
  }

  def sayOk() = {
    println("sayOKKK...")
  }

  def sub(n1: Int): Unit = {

  }

  /*请将List(3,5,7) 中的所有元素都 * 2 ，将其结果放到一个新的集合中返回，
    即返回一个新的List(6,10,14)
   */
  val list = List(3, 5, 7, 9)
  //说明 list.map(multiple)做了什么
  //1.将 list 这个集合的元素 依次遍历
  //2.将各个元素传递给 multiple 函数 => 新 Int
  //3.将得到新 Int,放入到一个新的集合并返回
  //4.因此 multiple 函数调用3
  val list2 = list.map(multiple)
  println("list2=" + list2) //List(6,10,14,18)
  def multiple(n: Int) = {
    println("multiple 被调用~~")
    2 * n
  }
}



