package com.actorAkkaDemo

import akka.actor.{ActorRef, ActorSystem}
import com.actorAkkaDemo.TradeMessage.{drawMoneyMsg, saveMoneyMsg}

/**
 * Created by hcy on 2017/4/24.
 */
object AkkaClient {
  private var systemActor: Option[ActorSystem] = None


  def main(args: Array[String]) {
     val sys = ActorSystem("AkkaClient")  //创建一个ActorSystem 对象
    systemActor = Some(sys)  // 将actor系统赋值给全局变量system
    val server = ServerBankActor.make(sys)  //创建出actorSystem的一个应用对象。
    server ! "init"
    processInput(server)  //
  }


  def processInput(server: ActorRef): Unit = {

    val addMoney = """^\s*([+])\s*(\d+)\s*(\w*)$""".r
    val drawMoney = """^\s*([-])\s*(\d+)\s*(\w*)$""".r

    val test = """^\s*+\d""".r

    def prompt() = print(">>")

    def invalidCommand(message: String) = {
      println(s">> $message")
    }
    def finished(): Nothing = exit("bye", 0)
    val handleLine: PartialFunction[String, Unit] = {

      case addMoney(c, n, s) => c match {
        case "+"  => server ! saveMoneyMsg(n.toInt, s)
        case _ => invalidCommand(c)
      }
      case drawMoney(c, n, s) => c match {
        case "-"  => server ! drawMoneyMsg(n.toLong, s)
        case _ => invalidCommand(c)
      }
      case test(s) => println(s)
      case "q" | "quit" | "exit" => finished()
      case string => println("未知的输入。。。。。")
    }

    while (true) {
      prompt()
      Console.in.readLine() match {
        case null => finished()
        case line => handleLine(line)
      }
    }

  }


  def exit(message: String, status: Int): Nothing = {
    for (sys <- systemActor) sys.terminate()
    println(s"exit: $message")
    sys.exit()
  }

}
