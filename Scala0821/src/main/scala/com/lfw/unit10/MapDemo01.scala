package com.lfw.unit10

object MapDemo01 {
  def main(args: Array[String]): Unit = {
    //1.默认 Map 是 immutable.Map
    //2.key-value 类型支持 Any
    //3.在 Map 的底层，每对 key-value 是 Tuple2
    val map1 = Map("Alice"->10,"Bob"->20,"Kotlin" ->"北京")
    println(map1)
  }
}
