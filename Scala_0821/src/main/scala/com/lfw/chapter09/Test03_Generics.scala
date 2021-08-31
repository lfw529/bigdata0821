package com.lfw.chapter09

object Test03_Generics {
  def main(args: Array[String]): Unit = {
    // 1. 协变和逆变
    val child: Parent = new Child
    val childList1: MyCollection1[SubChild] = new MyCollection1[Child]
    val childList2: MyCollection2[Parent] = new MyCollection2[Child]
    // 2. 上下限
    def test1[A <: Child](a: A): Unit = { //泛型上限
      println(a.getClass.getName)  //com.lfw.chapter09.SubChild
    }
    def test2[A >: Child](a: A): Unit = { //泛型的下限,传什么都能传过去,因为万物都是Any的子类
      println(a.getClass.getName)  //com.lfw.chapter09.SubChild
    }
    test1[SubChild](new SubChild)
//    test1[Child](new Parent)  error Parent > Child
//    test1[Parent](new Child)  //不符合方法test1的类型参数范围 [A <: com.lfw.chapter09.Child]
//    test1[SubChild](new Child)  error Child > SubChild
    /********************************************************/
//    test2[SubChild](new Child)  error Child > SubChild
//    test2[Child](new Parent)   error Parent > Child
    test2[Parent](new SubChild)
  }
}

// 定义继承关系
class Parent {}

class Child extends Parent {}

class SubChild extends Child {}

// 定义带泛型的集合类型
class MyCollection1[-E] {}
class MyCollection2[+E] {}
