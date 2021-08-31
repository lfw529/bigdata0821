package com.lfw { //包 com.lfw
  class User400 { //在com.lfw 包下创建个 User 类
    def sayHello():Unit={
      //想使用 com.lfw.scala2 包下的 Monster
      import com.lfw.scala2.Monster
      val monster = new Monster()
    }
  }
  package scala2 { //创建包 com.lfw.scala2
    class User400 { //在com.lfw.scala2 包下创建个 User 类
    }
    class Monster{

    }
  }

  package scala {

    class Person400 { //表示在 com.lfw.scala 下创建类 Person
      val name = "Nick"

      def play(message: String): Unit = {
        println(this.name + "" + message)
      }
    }

    class User400 {

    }

    object Test400 { //表示在 com.lfw.scala 创建object Test
      def main(args: Array[String]): Unit = {
        println("ok")
        //我们可以直接使用父包的内容
        //1.如果有同名的类，则采用就近原则来使用内容（比如包）
        //2.如果就是要使用父包的类，则指定路径即可
        val user = new User400
        println("user=" + user)
        val user2 = new com.lfw.User400()
        println("user2" +user2)
      }
    }
  }
}



