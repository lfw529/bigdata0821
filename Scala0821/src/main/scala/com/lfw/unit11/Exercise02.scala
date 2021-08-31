package com.lfw.unit11

import scala.collection.mutable.ArrayBuffer

object Exercise02 {
  def main(args: Array[String]): Unit = {
    val sentence = "AAAAAAAAAABBBBBBBBCCCCCDDDDDDD"
    val arrayBuffer = new ArrayBuffer[Char]()
    //理解折叠的第一个传入的 arrayBuffer 含义
    sentence.foldLeft(arrayBuffer)(putArray)
    println("arrayBuffer=" + arrayBuffer)
  }

  def putArray(arr: ArrayBuffer[Char], c: Char): ArrayBuffer[Char] = {
    //将 c 放入到 arr 中
    arr.append(c)
    arr
  }
}
