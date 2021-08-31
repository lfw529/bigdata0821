package chapter06

object Test02_PackageObject {
  def main(args: Array[String]): Unit = {
    commonMethod()
    println(commonValue)
  }
}

package chapter06{
  object Test02_PackageObject {
    def main(args: Array[String]): Unit = {
      commonMethod()
      println(commonValue)
    }
  }
}

package ccc{
  package ddd{
    object Test02_PackageObject{
      def main(args: Array[String]): Unit = {
        println(school)
      }
    }
  }
  package object ddd{ //同一层级

  }
}

// 定义一个包对象
package object ccc{  //包名和包对象须在同一层级，才能相互访问
  val school: String = "000"
}
