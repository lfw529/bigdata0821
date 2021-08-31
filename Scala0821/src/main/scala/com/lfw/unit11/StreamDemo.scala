package com.lfw.unit11

object StreamDemo {
  def main(args: Array[String]): Unit = {
    //创建 Stream
    def numsForm(n:BigInt):Stream[BigInt] = n#::numsForm(n+1)
    val stream1 = numsForm(1)
    println(stream1)
    //取出第一个元素
    println("head=" + stream1.head)
    println("tail=" + stream1.tail) //当对流执行 tail 操作时，就会产生一个新的数据
    println(stream1)
  }
}
