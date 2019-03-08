package io.github.gpetri.ch2

import akka.actor.SupervisorStrategy.{Escalate, Stop, Resume, Restart}
import akka.actor._
import scala.concurrent.duration._

object Messages {
  case class Add( a: Int, b: Int )
  case class Sub( a: Int, b: Int )
  case class Div( a: Int, b: Int )
}

class Calculator(printer: ActorRef) extends Actor {
  import Messages._

  override def preRestart(reason: Throwable, msg: Option[Any]) = {
    println("Calculator restarting because of ArithmeticException")
  }
  override def receive : Receive = {
    case Add(a, b) => printer ! s"sum is ${a + b}"
    case Sub(a, b) => printer ! s"sub is ${a - b}"
    case Div(a, b) => printer ! s"div is ${a / b}"
  }
}

class ResultPrinter extends Actor {
  override def preRestart(reason: Throwable, msg: Option[Any]) = {
    println("Printer is restarting too")
  }
  override def receive : Receive = {
    case msg => println(msg)
  }
}

class AllForOneStrategy extends Actor {
  import Messages._
  override val supervisorStrategy = AllForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 seconds) {
    case _ : ArithmeticException => Restart
    case _ : NullPointerException => Resume
    case _ : IllegalArgumentException => Stop
    case _ : Exception => Escalate
  }
  val printer = context.actorOf(Props[ResultPrinter], "PrinterActor")
  val calc = context.actorOf(Props(classOf[Calculator], printer), "Calculator")
  override def receive : Receive = {
    case "Start" => calc ! Add(10, 20)
      calc ! Sub(12, 10)
      calc ! Div(5, 2)
      calc ! Div(5, 0)
  }
}

object AllForOneStrategyApp extends App {
  val as = ActorSystem("AllForOneSystem")
  val supervisor = as.actorOf(Props[AllForOneStrategy], "SupervisorActor")
  supervisor ! "Start"
}
