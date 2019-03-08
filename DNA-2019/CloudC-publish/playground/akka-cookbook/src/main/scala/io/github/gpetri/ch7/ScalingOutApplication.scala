package io.github.gpetri.ch7

import akka.actor.{ActorSystem, ActorRef, Props}
import akka.routing.RoundRobinPool
import scala.concurrent.duration._

object ScalingOutWorker extends App {
  val as = ActorSystem("WorkerActorSystem")
  implicit val dpch = as.dispatcher
  val selection = as.actorSelection("akka.tcp://MasterActorSystem@127.0.0.1:2552/user/masterActor")
  selection.resolveOne(3 seconds).onSuccess {
    case master : ActorRef =>
      println("have actorref for master")
      val pool = RoundRobinPool(10)
      val workerPool =
        as.actorOf(Props[WorkerActor].withRouter(pool), "workerActor")
      master ! RegisterWorker(workerPool)
  }
}

object ScalingOutMaster extends App {
  val as = ActorSystem("MasterActorSystem")
  val master = as.actorOf(Props[MasterActor], "masterActor")
  (1 to 100).foreach( i => {
    master ! Work(s"$i")
    Thread.sleep(5000)
  })
}
