package io.github.gpetri.ch2

import akka.actor.{Actor, ActorSystem, Props, Terminated}

object MyMessages {
  case object Service
  case object Kill
}

class ServiceActor extends Actor {
  import MyMessages._

  override def receive : Receive = {
    case Service => println(s"Service!")
  }
}

class DeathWatchActor extends Actor {
  import MyMessages._

  val child = context.actorOf(Props[ServiceActor], "Service")
  context.watch(child)
  override def receive : Receive = {
    case Service => child ! Service
    case Kill =>
      context.stop(child)
    case Terminated(`child`) => println("The Service actor has been termianted")
  }
}

object DeathWatchApp extends App {
  import MyMessages._

  val as = ActorSystem("DeathWatchSystem")
  val dwa = as.actorOf(Props[DeathWatchActor], "DeathWatchActor")
  dwa ! Service
  dwa ! Service
  Thread.sleep(1000)
  dwa ! Kill
  dwa ! Service
}
