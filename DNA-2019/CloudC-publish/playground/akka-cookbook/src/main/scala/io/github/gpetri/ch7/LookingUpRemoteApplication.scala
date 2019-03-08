package io.github.gpetri.ch7

import akka.actor.{ActorRef, ActorSystem, Actor, Props}
import scala.concurrent.duration._

object LookingUpActorSelection extends App {
  val as = ActorSystem("LookingUpActors")
  implicit val dsch = as.dispatcher
  val selection = as.actorSelection("akka.tcp://LookingUpRemoteActors@127.0.0.1:2553/user/remoteActor")
  selection ! "test"
  selection.resolveOne(3 seconds).onSuccess {
    case ar : ActorRef =>
      print("We got an actorref")
      ar ! "test"
  }
}

object LookingUpRemoteActors extends App {
  val as = ActorSystem("LookingUpRemoteActors")
  as.actorOf(Props[SimpleActor], "remoteActor")
}
