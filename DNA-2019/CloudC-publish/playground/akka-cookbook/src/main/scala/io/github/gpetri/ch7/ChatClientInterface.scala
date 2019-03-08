package io.github.gpetri.ch7

import akka.actor.{Actor, ActorRef, Props}
import io.github.gpetri.ch7.ChatSever.Disconnect
import scala.io.StdIn._

object ChatClientInterface {
  case object Check
  def props(chatClient: ActorRef) = Props(new ChatClientInterface(chatClient))
}

class ChatClientInterface(chatClient : ActorRef) extends Actor {
  import ChatClientInterface._

  override def preStart() = {
    println("You are logged in. Type and press ret. 'DISCONNECT' to log out.")
    self ! Check
  }

  def receive = {
    case Check =>
      readLine() match {
        case "DISCONNECT" => 
          chatClient ! Disconnect
          println("Disconnecting")
          context.stop(self)
        case msg => 
          chatClient ! msg
          self ! Check
      }
  }
}
