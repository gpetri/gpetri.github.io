package io.github.gpetri.ch4

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._

class CompActor extends Actor {
  override def receive : Receive = {
    case (a: Int, b: Int) => sender ! (a + b)
  }
}

object FutureWithActor extends App {
  implicit val to = Timeout(10 seconds)
  val as = ActorSystem("FutureWithActorSystem")
  val computationActor = as.actorOf(Props[CompActor], "ComputationActor")
  val future = (computationActor ? (2, 3)).mapTo[Int]
  /* println(future) */
  val sum = Await.result(future, 10 seconds)
  println(s"The result is $sum")
}
