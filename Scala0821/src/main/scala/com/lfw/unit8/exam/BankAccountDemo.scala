package com.lfw.unit8.exam

object BankAccountDemo {
  /*
  扩展如下的BankAccount类，新类CheckingAccount对每次存款和取款都收取1美元的手续费
   */
  def main(args: Array[String]): Unit = {
    val checkingAccount = new CheckingAccount(100)
    checkingAccount.query()
    checkingAccount.withdraw(10)  //手续费1
    checkingAccount.query()  //89
    checkingAccount.deposit(10)//手续费1
    checkingAccount.query()  //98
  }
}

class BankAccount(initialBalance: Double) {
  private var balance = initialBalance

  def deposit(amount: Double) = {
    balance += amount;
    balance
  }

  def withdraw(amount: Double) = {
    balance -= amount;
    balance
  }

  //加入一个查询余额的方法
  def query(): Unit = {
    println("当前余额为：" + this.balance)
  }
}

class CheckingAccount(initialBalance: Double) extends BankAccount(initialBalance) {
  override def deposit(amount: Double): Double = super.deposit(amount - 1)

  override def withdraw(amount: Double): Double = super.withdraw(amount + 1)
}
