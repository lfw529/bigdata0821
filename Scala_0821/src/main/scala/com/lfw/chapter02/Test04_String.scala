package com.lfw.chapter02

object Test04_String {
  def main(args: Array[String]): Unit = {
    //(1)字符串，通过+号连接
    val name: String = "alice"
    val age: Int = 18
    println(age + "岁的" + name + "在大学学习")

    // *用于将一个字符串复制多次并拼接
    println(name * 3)

    //(2)printf用法：字符串，通过%传值：
    printf("%d岁的%s在大学学习\n", age, name)

    //(3)字符串模板（插值字符串）：通过$获取变量值
    println(s"${age}岁的${name}在大学学习")

    val num: Double = 2.3456
    println(f"The num is ${num}%2.2f") //格式化模板字符串：取小数点后两位
    println(raw"The num is ${num}%2.2f") //raw模板：代表原始字符串，只赋值变量，其他不变

    //(4)多行字符串：三引号""" """，stripMargin 默认是“|”作为连接符，代表边界忽略，不打印“|”之前的空格包括“|”本身
    val sql =
      s"""
         |select *
         |from
         |  student
         |where
         |  name = ${name}
         |and
         |  age > ${age}
         |""".stripMargin
    println(sql)
  }
}
