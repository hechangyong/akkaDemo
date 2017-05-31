package com.actorAkkaDemo

import akka.actor.Actor
import com.actorAkkaDemo.TradeMessage.{drawMoneyMsg, Response, saveMoneyMsg}

/**
 * Created by hcy on 2017/4/24.
 */
object BankActor {
  case object NoMessageException extends RuntimeException("NoMessage!")

}

class BankActor extends Actor {
  private val datastore = collection.mutable.Map.empty[String, Long]


  override def receive: Receive = {
    case saveMoneyMsg(amount, owner) =>
      if (datastore.contains(owner)) {
        datastore += owner -> (datastore(owner) + amount)
        println(s"【存款成功】已有用户： $owner,存入金额: $amount,账户现有金额: ${datastore(owner)}")
        sender ! Response("【存款成功】")

      } else {
        datastore += owner -> amount
        println(s"【存款成功】平台新增用户：$owner,存入金额：$amount")
        sender ! Response("【存款成功】")

      }
    case drawMoneyMsg(amount, owner) =>
      if (datastore.contains(owner)) {
        if (amount > datastore(owner)) {
          println(s"【余额不足】已有用户： $owner, 账户现有金额: ${datastore(owner)},要取现的金额为: $amount。")
          sender ! Response("【取现失败】")

        } else {
          datastore += owner -> (datastore(owner) - amount)
          println(s"【取现成功】已有用户： $owner, 要取现的金额为: $amount，取现后余额：${datastore(owner)}")
          sender ! Response("【取现成功】")

        }
      } else {
        println(s"【取现失败】平台没有该用户")
        sender ! Response("【取现失败】")
      }
    case _ =>  throw BankActor.NoMessageException
  }
}
