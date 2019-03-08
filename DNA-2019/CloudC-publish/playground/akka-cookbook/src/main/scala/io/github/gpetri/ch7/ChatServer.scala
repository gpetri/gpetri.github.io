package io.github.gpetri.ch7

import akka.actor.{Actor, ActorRef, Props, Terminated}

object ChatSever extends App {
  case object Connect
  case object Disconnect
  case object Disconnected
  case class Message( author: ActorRef, body: String, creationTS: Long = System.currentTimeMillis())
  def props = Props(new ChatServer())
}

class ChatServer extends Actor {
  import ChatSever._
  var onlineClients = Set.empty[ActorRef]
  def receive = {
    case Connect =>
      onlineClients += sender
      context.watch(sender)
    case Disconnect =>
      onlineClients -= sender
      context.unwatch(sender)
      sender ! Disconnected
    case Terminated(r) =>
      onlineClients -= r
    case msg : Message =>
      onlineClients.filter(_ != sender).foreach(_ ! msg)
  }
}
