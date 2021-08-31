package com.lfw.unit6.exam
import scala.language.postfixOps

object Test03 {
  /*
    针对下列Java循环编写一个Scala版本:
    for(int i=10;i>=0;i–)System.out.println(i);
   */
  def main(args: Array[String]): Unit = {
    for (i <- 0 to 10 reverse) {
      println(i)
    }
  }
}
