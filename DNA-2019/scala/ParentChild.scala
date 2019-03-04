package io.github.gpetri.ch2

import akka.actor.{ActorSystem, Actor, Props}

case object CreateChild
case class Greet(msg: String)

class ChildActor extends Actor {
  def receive : Receive = {
    case Greet(m) => println(s"My parent[${self.path.parent}] greeted to me [${self.path}] $m")
  }
}

class ParentActor extends Actor {
  def receive : Receive = {
    case CreateChild => 
      val ch = context.actorOf(Props[ChildActor], "Child")
      ch ! Greet("Hello Child")
  }
}

object ParentChildApp extends App {
  val as = ActorSystem("ParentChild")
  val par = as.actorOf(Props[ParentActor], "Parent")
  par ! CreateChild
}
