package com.lfw.unit7.myextends

object ScalaFieldOverrideDetail02 {
  def main(args: Array[String]): Unit = {
    println("xxx")
    val bbbbb = new BBBBB()
    println(bbbbb.sal) //0
    val b2: AAAAA = new BBBBB()
    println("b2.sal=" + b2.sal()) //0
    println("b2.sal=" + b2.sal("ss"))
  }
}

class AAAAA {
  def sal(): Int = {
    return 10
  }

  //如果加了参数的def,就不是重写了
  def sal(str: String): Int = {
    return 10
  }
}

class BBBBB extends AAAAA {
  override val sal: Int = 0 //底层 public sal
  //或者 override def sal: Int = 0,上面的 val 底层会多生成一个 final 变量

  override def sal(str: String): Int = 0  //ok
  //  override val sal(str: String): Int = 0  //报错
}