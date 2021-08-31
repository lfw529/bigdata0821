package com.lfw.unit2.datatype

object TypeDemo02 {
  def main(args: Array[String]): Unit = {
    println(sayHello)
  }


  //比如开发中，我们有一个方法，就会异常中断，这时可以返回 Nothing
  //即当我们 Nothing 做返回值，就是明确说明该方法没有正常返回值

  def sayHello: Nothing = {
    throw new Exception("抛出异常")
  }
}