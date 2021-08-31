//代码说明
//1.package com.lfw{} 表示我们创建了包 com.lfw,在{}中
//我们可以继续写它的子包 scala //com.lfw.scala,还可以写类，特质trait,还可以写 object
//2.即 scala 支持，在一个文件中，可以同时创建多个包，以及给各个包创建类，trait和object
package com.lfw { //包 com.lfw
  class User300 { //在com.lfw 包下创建个 User 类
  }
  package scala2 { //创建包 com.lfw.scala2
    class User300 { //在com.lfw.scala2 包下创建个 User 类
    }

  }

  package scala {

    class Person300 { //表示在 com.lfw.scala 下创建类 Person
      val name = "Nick"

      def play(message: String): Unit = {
        println(this.name + "" + message)
      }
    }

    class User300 {

    }

    object Test300 { //表示在 com.lfw.scala 创建object Test
      def main(args: Array[String]): Unit = {
        println("ok")
        //我们可以直接使用父包的内容
        //1.如果有同名的类，则采用就近原则来使用内容（比如包）
        //2.如果就是要使用父包的类，则指定路径即可
        val user = new User300
        println("user=" + user)
        val user2 = new com.lfw.User300()
        println("user2" +user2)
      }
    }
  }
}


