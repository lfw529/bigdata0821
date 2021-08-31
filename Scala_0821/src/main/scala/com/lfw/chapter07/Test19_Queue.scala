package com.lfw.chapter07

import scala.collection.immutable.Queue
import scala.collection.mutable

object Test19_Queue {
  def main(args: Array[String]): Unit = {
    //创建一个可变队列
    val queue: mutable.Queue[String] = new mutable.Queue[String]()
    //入队操作，可变长参数
    queue.enqueue("a", "b", "c")

    println(queue) //Queue(a, b, c)
    //出队操作
    println(queue.dequeue()) //a
    println(queue) //Queue(b, c)
    println(queue.dequeue()) //b
    queue.enqueue("d", "e")

    println(queue)  //Queue(c, d, e)
    println(queue.dequeue())  //c
    println(queue)  //Queue(d, e)

    println("----------------------------")

    //不可变队列
    val queue2: Queue[String] = Queue("a", "b", "c")
    //通过新的变量赋值来调用
    val queue3: Queue[String] = queue2.enqueue("d")
    println(queue2)  //Queue(a, b, c)
    println(queue3)  //Queue(a, b, c, d)
  }
}
