package com.lfw.unit2.datatype

object CharDemo {
  def main(args: Array[String]): Unit = {
    var char1: Char = 97
    //当我们输出一个 char 类型时，他会输出该数字对应的字符（码值表 unicode）//码值表包括 ascii
    println("char1=" + char1) //a

    //char 可以当作数字进行运行
    var char2: Char = 'a'
    var num = 10 + char2  //int型
    println("num=" + num) //107

    //原因和分析
    //1.当把一个计算的结果复制一个变量，则编译器会进行类型转换及判断（即会看范围 + 类型）【常量在编译期就会计算出结果】
    //2.当把一个字面量赋值一个变量，则编译器会进行范围的判定
//    var c2: Char = 'a' + 1  //报错  ，类型
//    var c3: Char = 97 + 1   //报错  ，类型
//    var c4: Char = 99999    //报错  ，范围
  }
}
