package io.github.gpetri.ch2

import akka.actor._
import akka.actor.SupervisorStrategy._
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.pattern.ask

case object Error
case class StopActor(actorRef: ActorRef)

class LifeCycleActor extends Actor {
  var sum = 1
  override def preRestart(reason: Throwable, message: Option[Any]) : Unit = {
    println(s"sum in preRestart is $sum")
  }

  override def preStart() : Unit = println(s"sum in preStart is $sum")

  override def receive = {
    case Error => throw new ArithmeticException()
    case _ => print("default message")
  }

  override def postStop() = print(s"sum in postStop is ${sum * 3}")

  override def postRestart(reason: Throwable) = {
    sum = sum * 2
    println(s"sum in postRestart is $sum")
  }
}

class Supervisor extends Actor {
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _ : ArithmeticException => Restart
    case t => super.supervisorStrategy.decider.applyOrElse(t, (_ : Any) => Escalate)
  }
  def receive = {
    case (props : Props, name: String) =>
      sender ! context.actorOf(props, name)
    case StopActor(actorRef) => context.stop(actorRef)
  }
}

object ActorLifeCycleApp extends App {
  implicit val to : Timeout = Timeout(2 seconds)
  val as = ActorSystem("ActorLifeCycle")
  val sv = as.actorOf(Props[Supervisor], "Supervisor")
  val chFuture = sv ? (Props(new LifeCycleActor), "LifeCycleActor")
  val child = Await.result(chFuture.mapTo[ActorRef], 2 seconds)
  child ! Error
  Thread.sleep(1000)
  sv ! StopActor(child)
}
