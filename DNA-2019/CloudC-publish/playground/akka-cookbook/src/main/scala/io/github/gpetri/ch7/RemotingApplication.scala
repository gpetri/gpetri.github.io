package io.github.gpetri.ch7

import akka.actor.{ActorSystem, Props}

object HelloAkkaRemoting1 extends App {
  val as = ActorSystem("HelloAkkaRemoting1")
}

object HelloAkkaRemoting2 extends App {
  val as = ActorSystem("HelloAkkaRemoting2")
  println("Creating actor from HelloAkkaRemoting2")
  val ac = as.actorOf(Props[SimpleActor], "simpleRemoteActor")
  val ac1 = as.actorOf(Props[SimpleActor], "simpleActor")
  ac ! "Checking"
  ac1 ! "Checking"
}
