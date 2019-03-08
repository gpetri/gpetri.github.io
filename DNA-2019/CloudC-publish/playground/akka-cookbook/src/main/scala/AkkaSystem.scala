package io.github.gpetri.ch1

import akka.actor.{ActorSystem, Props, ActorRef, Actor}

class SillyActor extends Actor {
  override def receive : Receive = {
    case _ => println("Not implemented")
  }

}

object AkkaSystem extends App {
  val as = ActorSystem("ActorSystem")
  println(as)
  val ar = as.actorOf(Props[SillyActor], "Gustavo")
  println("Actor path", ar.path)
}
