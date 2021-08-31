package com.lfw.chapter07

import java.util.concurrent.SynchronousQueue

import scala.collection.immutable
import scala.collection.parallel.immutable.ParSeq

object Test20_Parallel {
  def main(args: Array[String]): Unit = {
    //串行集合
    //(1 to 100) 本身是一个 Range，带索引的集合
    val result: immutable.IndexedSeq[Long] = (1 to 100).map(
      x => Thread.currentThread.getId
    )
    println(result) //Vector(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
    //并行集合的计算 par
    val result2: ParSeq[Long] = (1 to 100).par.map(
      x => Thread.currentThread.getId
    )
    println(result2)//ParVector(12, 12, 12, 18, 18, 18, 18, 18, 18, 18, 18, 18, 16, 16, 16, 16, 16, 16, 19, 12, 12, 13, 13, 13, 13, 14, 14, 14, 15, 13, 13, 17, 14, 14, 18, 19, 19, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19)
  }
}
