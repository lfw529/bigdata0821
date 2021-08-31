package com.lfw.distribute

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.{ServerSocket, Socket}

object Server  extends CoreApp {

  def main(args: Array[String]): Unit = {
    core("serverSocket"){
      //处理envData
      val server: ServerSocket = envData.asInstanceOf[ServerSocket]
      //监听客户端的连接
      while(true){
        val client: Socket = server.accept()
        //将每个客户端的通信交给具体的线程负责
        new Thread(new Runnable {
          // 重写run方法
           def run(): Unit = {
              // In
             val in = new ObjectInputStream(client.getInputStream)
             val readResult: AnyRef = in.readObject()
             var task: Task = readResult.asInstanceOf[Task]

             // 计算
             val result: Int = task.cal()
             task = null
             // 关闭输入对象
             client.shutdownInput()

             // Out
             val out = new ObjectOutputStream(client.getOutputStream)
             out.writeObject(result)
             out.flush()
             client.shutdownOutput()

             // 关闭client对象
            client.close()

           }
        }).start()
      }
    }
  }
}
