package io.github.gpetri.ch1

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import scala.util.Random._

object Messages {
  case class Done( result: Int )
  case class GetFibNumber( input: Int )
  case class Start( actorRef: ActorRef )
}


class FibResonder extends Actor {
  import Messages._
  override def receive: Receive = {
    case GetFibNumber(n) =>
      val fn = fibonacci(n)
      /* Thread.sleep(2000) */
      sender ! Done(fn)
    case _ =>
      println("Erroneous message")
  }

  def fibonacci(n : Int) : Int =  n match {
    case 0 | 1 => 1
    case _ => fibonacci(n-1) + fibonacci(n-2)
  }
}

class QueryActor extends Actor {
  import Messages._

  override def receive : Receive = {
    case Start(ar : ActorRef) =>
      val x = nextInt(10)
      println(s"get Fibo for $x")
      ar ! GetFibNumber(x)
    case Done(n : Int) =>
      println(s" The answer was $n")
  }
}

object Communication extends App {
  import Messages._

  val as = ActorSystem("Communication")
  val aFib = as.actorOf(Props[FibResonder], "FiboResponder")
  val qa = as.actorOf(Props[QueryActor], "queryActor")
  qa ! Start(aFib)
  /* as.terminate() */
}
