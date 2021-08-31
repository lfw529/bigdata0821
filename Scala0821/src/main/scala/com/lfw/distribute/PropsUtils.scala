package com.lfw.distribute

import java.io.InputStream
import java.util.{Properties, ResourceBundle}

object PropsUtils {

  //读取文件
  /*
  private val stream: InputStream =
    PropsUtils.getClass.getClassLoader.getResourceAsStream("framework.properties")
  val props : Properties = new Properties
  props.load(stream)
  */

  private val bundle: ResourceBundle = ResourceBundle.getBundle("framework")

  def getValue(key:String):String={
      //props.getProperty(key)
    bundle.getString(key)
  }
}
