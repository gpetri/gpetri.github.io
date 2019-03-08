import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorSystem, Props, OneForOneStrategy}

class Printer extends Actor {

  override def preRestart(reason: Throwable, msg: Option[Any]) : Unit = {
    println(s"Printer: Restarting because of an Arithmetic Exception")
  }

  override def receive : Receive = {
    case msg : String => println(s"Printer $msg")
    case msg : Int => 1 / 0 // always fails with arithmetic exception
  }
}

class IntAdder extends Actor {
  var x = 0

  override def receive : Receive = {
    case msg: Int =>
      x = x + msg
      println(s"IntAdder: sum is $x")
    case msg: String => throw new IllegalArgumentException
  }

  override def postStop = {
    println("IntAdder: stopping because I got a string message")
  }
}

class SupervisionStrategy extends Actor {
  import scala.concurrent.duration._

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _ : ArithmeticException => Restart
    case _ : NullPointerException => Resume
    case _ : IllegalArgumentException => Stop
    case _ : Exception => Escalate
  }

  val printer = context.actorOf(Props[Printer], "PrinterActor")
  val intAdder = context.actorOf(Props[IntAdder], "IntAdderActor")
  override def receive : Receive = {
    case "Start" => printer ! "Hello PrinterActor"
      printer ! 10
      intAdder ! 10
      intAdder ! 10
      intAdder ! "Hello IntAdderActor"
  }
}

object SupervisionStrategyApp extends App {
  val as = ActorSystem("SupervisionStrategySystem")
  as.actorOf(Props[SupervisionStrategy], "SStrategy") ! "Start"
}
