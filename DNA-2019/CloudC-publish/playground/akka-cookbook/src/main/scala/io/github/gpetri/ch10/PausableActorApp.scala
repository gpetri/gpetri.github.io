package io.github.gpetri.ch10

import akka.actor.{ActorSystem, Props}
import io.github.gpetri.ch10.PausableActor.Work

object PausableActorApp extends App {
  val actorSystem = ActorSystem()
  val hardWorker = actorSystem.actorOf(Props[HardWorker], "hardWorker")
  val pausableHardWorker = actorSystem.actorOf(Props(classOf[PausableActor], hardWorker), "pausableActor")

  (1 to 100).foreach { i =>
    pausableHardWorker ! Work(i)
  }
}
