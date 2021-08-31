//代码说明
//1.package com.lfw{} 表示我们创建了包 com.lfw,在{}中
//我们可以继续写它的子包 scala //com.lfw.scala,还可以写类，特质trait,还可以写 object
//2.即 scala 支持，在一个文件中，可以同时创建多个包，以及给各个包创建类，trait和object
package com.lfw { //包 com.lfw
  package scala { //包 com.lfw.scala ,如果写的不是子包，则会覆盖外层的包路径
    class Person100 { //表示在 com.lfw.scala 下创建类Person
      val name = "Nick"

      def play(message: String): Unit = {
        println(this.name + " " + message)
      }
    }

    object Test100 { //表示在 com.lfw.scala 创建object Test
        def main(args: Array[String]): Unit = {
        println("ok")
      }
    }
  }

}

