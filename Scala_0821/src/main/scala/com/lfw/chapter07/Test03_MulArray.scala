package com.lfw.chapter07

object Test03_MulArray {
  def main(args: Array[String]): Unit = {
    //1.创建二维数组
    val array: Array[Array[Int]] = Array.ofDim[Int](2,3)

    //2.访问元素
    array(0)(2) = 19
    array(1)(0) = 25

    println(array.mkString(","))  //[I@1e643faf,[I@34ce8af7
    println("------------------------")
    for (i <- 0 until array.length; j <- 0 until array(i).length){
      println(array(i)(j))
    }
    for (i <- array.indices; j <- array(i).indices){
      print(array(i)(j) + "\t")
      if(j == array(i).length - 1) println()
      //      0	 0	19
      //      25 0	0
    }
    println("-----------------------------")
    array.foreach(line => line.foreach(println))
    println("-----------------------------")
    //省略版
    array.foreach(_.foreach(println))
  }
}
