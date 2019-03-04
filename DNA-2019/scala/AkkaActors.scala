package io.github.gpetri.ch1

import akka.actor.ActorSystem

object AkkaActorSystem extends App {
  val as = ActorSystem("ActorSystem")
  println(as)
}
