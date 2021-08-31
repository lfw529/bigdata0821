package com.lfw.unit8.exam

/*
  编写一个Conversions对象，加入 inchesToCentimeters,gallonsToLiters 和 milesToKilometers 方法
 */
object Conversions{
  //英寸->厘米
  def inchesToCentimetres(value: Double) = value * 2.54

  //加仑->升
  def gallonsToLiters(value: Double) = value * 3.78541178

  //英里->公里
  def milesToKilometers(value: Double) = value * 1.6909344
}
