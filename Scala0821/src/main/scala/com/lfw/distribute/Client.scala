package com.lfw.distribute

import java.io.{ObjectInputStream, ObjectOutputStream}
import java.net.Socket

object Client extends CoreApp {

  def main(args: Array[String]): Unit = {

    core("socket"){
        // 处理envData
        val client: Socket = envData.asInstanceOf[Socket]
        // 发送的数据
        //val fun:Int=>Int = { _ * 2 }
        val task = new Task
        task.data = 1000
        val num = 30
        task.fun = _ * num
        // out
        val out = new ObjectOutputStream(client.getOutputStream)
        out.writeObject(task)
        out.flush()
       // 关闭输出对象
        client.shutdownOutput()


        // In
        val in = new ObjectInputStream(client.getInputStream)
        val readResult: AnyRef = in.readObject()

        println("接收到计算结果是: " + readResult)
        client.shutdownInput()
    }
  }
}
