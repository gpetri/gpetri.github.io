package io.github.gpetri.ch1

import akka.actor.{ActorSystem, Props, PoisonPill, Actor}

case object Stop

class ShutdownActor extends Actor {
  override def receive : Receive = {
    case m: String => println(s"$m")
    case Stop => context.stop(self)
  }
}

object ShutdownActorApp extends App {
  val as = ActorSystem("ShutdownActor")
  val sd = as.actorOf(Props[ShutdownActor], "ShutMeDown")
  sd ! "Hello Actor"
  sd ! PoisonPill
  sd ! "Hello Again"
  val sd1 = as.actorOf(Props[ShutdownActor], "ShutMeDown1")
  sd1 ! "Hello Actor"
  sd1 ! Stop
  sd1 ! "Hello Again"
}
