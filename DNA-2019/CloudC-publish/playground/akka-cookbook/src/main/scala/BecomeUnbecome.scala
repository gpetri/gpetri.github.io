package io.github.gpetri.ch1

import akka.actor.{Actor, ActorSystem, Props}

class BecomeUnbecome extends Actor {
  override def receive : Receive = {
    case true => context.become(isStateTrue)
    case false => context.become(isStateFalse)
  }

  def isStateTrue: Receive = {
    case msg : String => println(s"$msg and the is true")
    case false => context.become(isStateFalse)
  }

  def isStateFalse: Receive = {
    case x : Int => println(s"$x and the state is false")
    case true => context.become(isStateTrue)
  }
}

object BecomeUnbecomeApp extends App {
  val as = ActorSystem("BecomeUnbecome")
  val bu = as.actorOf(Props[BecomeUnbecome], "BecomeUnbecomeActor")
  bu ! true
  bu ! "hello"
  bu ! false
  bu ! 1000
}
