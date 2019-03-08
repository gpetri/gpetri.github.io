package io.github.gpetri.ch7

import akka.actor.{Actor, ActorRef, Props}
import io.github.gpetri.ch7.ChatSever.{Connect, Disconnect, Disconnected, Message}
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout
import scala.concurrent.duration._

object ChatClient {
  def props(chatServer: ActorRef) = Props(new ChatClient(chatServer))
}


class ChatClient(chatServer: ActorRef) extends Actor {
  import context.dispatcher
  implicit val to = Timeout(5 seconds)
  override def preStart = { chatServer ! Connect }

  def receive = {
    case Disconnect => 
      (chatServer ? Disconnect).pipeTo(self)
    case Disconnected => 
      context.stop(self)
    case body : String => 
      chatServer ! Message(self, body)
    case msg : Message => 
      println(s"Message from [${msg.author}] at [${msg.creationTS}]: ${msg.body}")
  }
}

