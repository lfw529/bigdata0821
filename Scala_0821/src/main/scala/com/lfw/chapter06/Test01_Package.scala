package com{
  import com.lfw.scala.Inner  //外部类访问内部类的属性，需要导包

  //在外层包中定义单例对象
  object Outer{
    var out:String = "out"

    def main(args: Array[String]): Unit = {
      println(Inner.in)
    }
  }
  package lfw{
    package scala{
      //内层包中定义单例对象
      object Inner{
        var in: String = "in"
        def main(args: Array[String]): Unit = {
          //子包中的类可以直接访问父包中的内容，而无需导包
          println(Outer.out)
          Outer.out = "outer"
          println(Outer.out)
        }
      }
    }
  }
}

// 在同一文件中定义不同的包
package aaa{
  package bbb{
    object Test01_Package {
      def main(args: Array[String]): Unit = {
        import com.lfw.scala.Inner   //外部环境下需要导包
        println(Inner.in)
      }
    }
  }
}

