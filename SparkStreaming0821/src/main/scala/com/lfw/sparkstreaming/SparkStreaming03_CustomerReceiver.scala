package com.lfw.sparkstreaming
import java.io.{BufferedReader, InputStream, InputStreamReader}
import java.net.Socket
import java.nio.charset.StandardCharsets
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming03_CustomerReceiver {
  def main(args: Array[String]): Unit = {
    //TODO 1 创建SparkConf配置文件,并设置App名称
    val conf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    //TODO 2 利用SparkConf创建StreamingContext对象
    val ssc = new StreamingContext(conf, Seconds(3))

    val lineDStream: ReceiverInputDStream[String] = ssc.receiverStream(new CustomerReceiver("hadoop105", 9999))

    val resultDStream = lineDStream.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    resultDStream.print()

    //TODO 3 启动StreamingContext,并且阻塞主线程,一直执行
    ssc.start()
    ssc.awaitTermination()
  }
}

class CustomerReceiver(host: String, port: Int) extends Receiver[String](StorageLevel.MEMORY_ONLY) {
  //receiver 刚启动的时候，调用该方法，作用为：读数据并将数据发送给Spark
  override def onStart(): Unit = {
    //在 onStart 方法里面创建一个线程，专门用来接收数据
    new Thread("Socket Receiver") {
      override def run() {
        receive()
      }
    }.start()
  }

  //数据接受方法，实现具体的数据接收逻辑
  def receive(): Unit = {
    val socket = new Socket(host, port)
    val inputStream: InputStream = socket.getInputStream
    //字节流不能按行读取，获取数据比较麻烦
    val reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))

    var line: String = reader.readLine()
    //当receiver没有关闭且输入数据不为空，就循环发送数据给Spark
    while (line != null && !isStopped()) {
      store(line)
      line = reader.readLine()
    }
    reader.close()
    socket.close()

    restart("restart")
  }

  override def onStop(): Unit = {
  }
}