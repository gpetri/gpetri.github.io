package io.github.gpetri.ch1

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorSystem

class SummingActor extends Actor {
  var sum = 0

  override def receive: Receive = {
    case x : Int =>
      sum = sum + x
      println(s"my state is $sum")
    case _ => println("Say what?!")
  }
}

class SummingActorWithConstructor(val n: Int) extends Actor {
  var sum = 0

  override def receive: Receive = {
    case x: Int =>
      sum = sum + x
      println(s"my state is ${ n + sum }")
    case _ => println("Say what?!")
  }
}

object BehaviorAndState extends App {
  val as = ActorSystem("BehaviorAndStateSystem")
  val actor1 = as.actorOf(Props[SummingActor], "SummingActor")
  val actor2 = as.actorOf(Props(classOf[SummingActorWithConstructor], 10), "SummingActorWS")
  println(actor1.path)
  actor1 ! 1
  1 to 10 foreach (_ => {
    Thread.sleep(1000)
    actor1 ! 1
  })
  actor2 ! 2
}
