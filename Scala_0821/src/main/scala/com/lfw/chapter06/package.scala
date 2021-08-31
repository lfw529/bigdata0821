package object chapter06 {
  // 定义当前包共享的属性和方法
  val commonValue = "lfw"
  def commonMethod() = {
    println(s"我们在${commonValue}学习")
  }
}
