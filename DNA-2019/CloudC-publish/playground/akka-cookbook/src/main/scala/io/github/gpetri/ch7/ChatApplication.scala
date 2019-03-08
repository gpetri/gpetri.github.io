package io.github.gpetri.ch7

import akka.actor.{ActorRef, ActorSystem, Props}
import scala.concurrent.duration._

object ChatClientApplication extends App {
  val as = ActorSystem("ChatClient")
  implicit val dspch = as.dispatcher
  val chatServerAddress = "akka.tcp://ChatServer@127.0.0.1:2552/user/chatServer"
  as.actorSelection(chatServerAddress).resolveOne(3 seconds).onSuccess {
    case chatSever : ActorRef => {
      val client = as.actorOf(ChatClient.props(chatSever), "chatClient")
      as.actorOf(ChatClientInterface.props(client), "chatClientInterface")
    }
  }
}

object ChatServerApplication extends App {
  val as = ActorSystem("ChatServer")
  as.actorOf(ChatSever.props, "chatServer")
}
