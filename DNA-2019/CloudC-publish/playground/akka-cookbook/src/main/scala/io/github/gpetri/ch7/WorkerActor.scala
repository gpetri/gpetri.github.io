package io.github.gpetri.ch7

import akka.actor.{Actor, ActorRef, Terminated}
import scala.util.Random

case class Work(workId: String)
case class WorkDone(workId: String) 

class WorkerActor extends Actor {
  def receive = {
    case Work(workId) => 
      Thread.sleep(3000)
      sender ! WorkDone(workId)
      println(s"Work done for $workId")
  }
}

case class RegisterWorker(workerActor : ActorRef)

class MasterActor extends Actor {
  var workers = List.empty[ActorRef]
  def receive = {
    case RegisterWorker(wa) =>
      context.watch(wa)
      workers = wa :: workers
    case Terminated(ar) => 
      println(s"Actor ${ar.path.address} terminated, removing from workers")
      workers = workers.filterNot(_ == ar)
    case work: Work if workers.isEmpty =>
      println("Not more workers to process work")
    case work: Work => 
      workers(Random.nextInt(workers.size)) ! work
    case WorkDone(workId) =>
      println(s"work done: $workId")
  }
}
