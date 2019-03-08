package io.github.gpetri.ch7

import akka.actor.{ActorSystem, Deploy, Props, Address}
import akka.remote.RemoteScope

object RemoteActorsProgrammatically1 extends App {
  val as = ActorSystem("RemoteActorsProgrammatically1")
}

object RemoteActorsProgrammatically2 extends App {
  val as = ActorSystem("RemoteActorsProgrammatically2")
  println("Creating from RemoteActorsProgrammatically2")
  val address = Address("akka.tcp", "RemoteActorsProgrammatically1", "127.0.0.1", 2552)
  val ac = as.actorOf(Props[SimpleActor].withDeploy(Deploy(scope = RemoteScope(address))), "remoteActor")
  ac ! "Checking"
}
