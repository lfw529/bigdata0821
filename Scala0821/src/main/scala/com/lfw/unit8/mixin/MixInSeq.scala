package com.lfw.unit8.mixin

object MixInSeq {
  def main(args: Array[String]): Unit = {

    //这时 FF 是这样形式 class FF extends with CC with DD
    val ff1 = new FF()
    println(ff1)
    println("--------------")
    val ff2 = new KK with CC with DD
    println(ff2)
    /*运行结果
      E....
      A...
      B....
      C....
      D....
      F....
      com.lfw.unit8.mixin.FF@5fe5c6f
      --------------
      E....
      K....
      A...
      B....
      C....
      D....
      com.lfw.unit8.mixin.MixInSeq$$anon$1@6979e8cb
  */
  }
}

trait AA{
  println("A....")
}

trait BB extends AA{
  println("B....")
}

trait CC extends BB {
  println("C....")
}

trait DD extends BB{
  println("D....")
}

class EE{
  println("E....")
}

class FF extends EE with CC with DD{ //先继承了EE类，然后再继承CC和DD
  println("F....")
}

class KK extends EE {  //KK直接继承了普通类
  println("K....")
}