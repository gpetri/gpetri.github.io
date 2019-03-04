package io.github.gpetri.ch4

import akka.actor.{Actor, ActorSystem, Props}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class FutureActor extends Actor {
  import context.dispatcher
  override def receive : Receive = {
    case (a:Int, b:Int) =>
      val f = Future(a + b)
      val res = Await.result(f, 10 seconds)
      println(res)
  }
}


object FutureInsideActor extends App {
  val as = ActorSystem("")
  val futActor = as.actorOf(Props[FutureActor])
  futActor ! (10, 20)
}
