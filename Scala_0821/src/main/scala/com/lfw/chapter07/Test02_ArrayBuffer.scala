package com.lfw.chapter07

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Test02_ArrayBuffer {
  def main(args: Array[String]): Unit = {
    //1.创建可变数组
    val arr1: ArrayBuffer[Int] = new ArrayBuffer[Int]() //动态创建
    val arr2 = ArrayBuffer(23, 57, 92)

    println(arr1)
    println(arr2)

    //2.访问元素
    //    println(arr1(0)) //error，索引越界
    println(arr2(1))
    arr2(1) = 39 //可变数组修改值
    println(arr2(1))
    println("======================")
    // 3. 添加元素
    val newArr1: mutable.Seq[Int] = arr1 :+ 15
    println(arr1) //ArrayBuffer()
    println(newArr1) //ArrayBuffer(15)  添加了一个元素的newArr1
    println(arr1 == newArr1) //false ,两个数组引用不同

    val newArr2 = arr1 += 19 //将地址赋值拷贝，引用相同
    println(arr1) //ArrayBuffer(19)
    println(newArr2) //ArrayBuffer(19)
    println(arr1 == newArr2) //两个数组引用相同
    newArr2 += 13
    println(arr1) //ArrayBuffer(19, 13)
    //在集合头部加上 77，一般可变用方法，不可变数组用符号增删
    77 +=: arr1
    println(arr1) //ArrayBuffer(77, 19, 13)
    println(newArr2) //ArrayBuffer(77, 19, 13)
    //在可变集合末尾插入 36
    arr1.append(36)
    //在可变集合末尾插入 11 76
    arr1.prepend(11, 76)
    //insert 方法中 1 为数组下表索引，即在第一个元素之后插入 13 59 两个元素
    arr1.insert(1, 13, 59)
    println(arr1) //ArrayBuffer(11, 13, 59, 76, 77, 19, 13, 36)

    arr1.insertAll(2, newArr1)   //在2号位置后增加 newArr1
    println(arr1)  //ArrayBuffer(11, 13, 15, 59, 76, 77, 19, 13, 36)

    //将可遍历对象中包含的元素添加到此缓冲区,从头添加。
    arr1.prependAll(newArr2)
    println(arr1)  //ArrayBuffer(11, 13, 15, 59, 76, 77, 19, 13, 36, 11, 13, 15, 59, 76, 77, 19, 13, 36)
    println("-------------------------------")
    // 4. 删除第三个元素
    arr1.remove(3)
    println(arr1)  //ArrayBuffer(11, 13, 15, 76, 77, 19, 13, 36, 11, 13, 15, 59, 76, 77, 19, 13, 36)
    //删除 从第一个元素后的10个元素 ，0代表下标索引 ，10 代表删除个数
    arr1.remove(0, 10)
    println(arr1)  //ArrayBuffer(15, 59, 76, 77, 19, 13, 36)
    //删除元素值等于13的第一个元素
    arr1 -= 13
    println(arr1)  //ArrayBuffer(15, 59, 76, 77, 19, 36)

    // 5. 可变数组转换为不可变数组
    val arr: ArrayBuffer[Int] = ArrayBuffer(23, 56, 98)
    val newArr: Array[Int] = arr.toArray
    println(newArr.mkString(", "))//23, 56, 98
    println(arr)  //ArrayBuffer(23, 56, 98)
    println("------------------------")
    // 6. 不可变数组转换为可变数组ArrayBuffer
    val buffer: mutable.Buffer[Int] = newArr.toBuffer
    println(buffer)  //ArrayBuffer(23, 56, 98)
    println(newArr)  //[I@6108b2d7
  }
}
