package io.github.gpetri.ch1

import scala.concurrent.Await
import scala.concurrent.duration._

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

class FiboActor extends Actor {
  override def receive: Receive = {
    case n : Integer =>
      val fn = fibonacci(n)
      /* Thread.sleep(2000) */
      sender ! fn
    case _ =>
      println("Erroneous message")
  }

  def fibonacci(n : Int) : Int =  n match {
    case 0 | 1 => 1
    case _ => fibonacci(n-1) + fibonacci(n-2)
  }
}


object FiboActorApp extends App {
  implicit val to = Timeout(10 seconds)
  val as = ActorSystem("FiboSystem")
  val fibact = as.actorOf(Props[FiboActor], "FibMaker")
  /* val fibact2 = as.actorOf(Props[FiboActor], "FibMaker2") */
  val future = (fibact ? 3).mapTo[Int]
  val fnum = Await.result(future, 1 seconds)
  println(s"the result is $fnum")
}
