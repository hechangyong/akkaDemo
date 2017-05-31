package com.actorAkkaDemo

import akka.actor.{ActorSystem, Props, ActorRef, Actor}
import com.actorAkkaDemo.TradeMessage.{Response, Request}

/**
 * Created by hcy on 2017/4/24.
 */
class ServerBankActor extends Actor {

  var serverBankActor: ActorRef = null

  def initWorkerActor = {
    println("初始化银行工作actor")
    serverBankActor = context.actorOf(Props[BankActor], s"ServerBankActor")
    println(serverBankActor)
  }

  override def receive: Receive = {
    case "init" => initWorkerActor
    case request: Request =>
      println("分发了。。。。。。")
      serverBankActor  ! request
    case Response(message) => println("接受到返回,返回内容为："+message)
  }
}

object ServerBankActor {
  def make(system: ActorSystem): ActorRef =
    system.actorOf(Props[ServerBankActor], "server")
}