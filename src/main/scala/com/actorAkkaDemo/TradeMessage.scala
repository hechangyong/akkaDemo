package com.actorAkkaDemo

/**
 * Created by hcy on 2017/4/24.
 */
object TradeMessage {
  sealed trait Request {
    val money: Long
    val ownerName: String
  }

  case class saveMoneyMsg(money: Long, ownerName: String) extends Request

  case class drawMoneyMsg(money: Long, ownerName: String) extends Request

  case class Response(message: String)

}
