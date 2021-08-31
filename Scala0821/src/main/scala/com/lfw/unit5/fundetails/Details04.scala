package com.lfw.unit5.fundetails

object Details04 {
  def main(args: Array[String]): Unit = {
    println(sayOk("mary"))
  }

  //name 形参的默认值 jack
  def sayOk(name: String = "jack"): String = {
    return name + "ok!"
  }
}
