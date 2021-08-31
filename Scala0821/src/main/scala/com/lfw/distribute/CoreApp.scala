package com.lfw.distribute

import java.net.{ServerSocket, Socket}


class CoreApp {

  var envData:Any = _
  /**
    * 思想: 设计一个方法,
    *    1. 可以获取各种环境。例如 Socket  ServerSocket  Jdbc ....
    *
    *    2. 该方法可以接受外部传入的逻辑代码。进行执行等操作
    *
    *    3. 释放各种环境
    */
   def core(env:String)(op: =>Unit): Unit ={
      // 1. 按照传入的env来获取环境
     if(env == "socket"){
        envData = initSocket()
     }else if (env == "serverSocket"){
        envData = initServerSocket()
     }else if( env == "xxxxx"){

     }

     // 2. 执行核心逻辑代码
     try {
        op
     } catch {
       case ex: Exception => println("执行核心逻辑抛出异常 ：" +ex.getMessage)
     }


     //3. 释放环境
     if(env == "socket"){
       closeSocket()
     }else if (env == "serverSocket"){
       closeServerSocket
     }else if( env == "xxxxx"){

     }

   }

   def closeServerSocket():Unit = {
     val server: ServerSocket = envData.asInstanceOf[ServerSocket]
     if(!server.isClosed){
       server.close()
     }
   }

   def closeSocket():Unit={
      val socket: Socket = envData.asInstanceOf[Socket]
      if(!socket.isClosed){
        socket.close()
      }
   }

   def initSocket():Socket={
      new Socket(PropsUtils.getValue("server.host"),
         PropsUtils.getValue("server.port").toInt)
   }

   def initServerSocket():ServerSocket={
      new ServerSocket(PropsUtils.getValue("server.port").toInt)

   }

}
