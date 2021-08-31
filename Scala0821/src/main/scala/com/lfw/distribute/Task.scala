package com.lfw.distribute

// Scala的类型默认都已经支持了序列化
class Task extends Serializable {  // Serializable当成一个"特质"来用。
   var data:Int = _
   var fun:Int=>Int = null

   def cal(): Int ={
     fun(data)
   }
}
