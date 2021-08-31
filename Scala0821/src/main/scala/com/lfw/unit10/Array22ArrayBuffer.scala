package com.lfw.unit10

import scala.collection.mutable.ArrayBuffer

object Array22ArrayBuffer {
  def main(args: Array[String]): Unit = {
    val arr2 = ArrayBuffer[Int]()
    //追加值
    arr2.append(1, 2, 3)
    println(arr2)  //ArrayBuffer(1, 2, 3)

    //说明
    //1.arr2.toArray 调用 arr2 的方法 toArray
    //2.将 ArrayBuffer --->Array
    //3.arr2 本身没有任何变化
    val newArr = arr2.toArray
    println(newArr)    //[I@649d209a

    //说明
    //1.new Arr.toBuffer 是把 Array->Buufer
    //2.底层的实现
   /* override def toBuffer[A1 >: A]:mutable.Buffer[A1] = {
      val result = new mutable.ArrayBuffer[A1](size)
      copyToBuffer(result)
      result
    }*/
    //3.newArr 本身没变化
    val newArr2 = newArr.toBuffer
    newArr2.append(123)
    println(newArr2)  //ArrayBuffer(1, 2, 3, 123)
  }
}