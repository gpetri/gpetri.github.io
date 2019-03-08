package io.github.gpetri.ch2

import akka.actor._
import scala.collection.mutable.ListBuffer
import scala.util.{ Random }

object Messages1 {
  case class DoubleValue(x: Int)
  case object CreateChild
  case object Send
  case class Response(x: Int)
}

class DoubleActor extends Actor {
  import Messages1._
  def receive = {
    case DoubleValue(number) => 
      println(s"${self.path.name} got the number $number")
      sender ! Response(number * 2)
  }
}

class ParentCollectActor extends Actor {
  import Messages1._

  val rnd = new Random
  var childs = ListBuffer[ActorRef]()

  def receive = {
    case CreateChild => 
      childs ++= List(context.actorOf(Props[DoubleActor]))
    case Send => {
      println(s"Sending message to child")
      childs.zipWithIndex map {
        case (child, value) => child ! DoubleValue(Random.nextInt(10))
      }
    }
    case Response(x) => { println(s"Parent: response from child ${sender.path.name} is $x") }
  }
}

object SendMessagesToChildrenApp extends App {
  import Messages1._

  val as = ActorSystem("SendMessagesToChildrenApp")
  val parent = as.actorOf(Props[ParentCollectActor], "Parent")
  parent ! CreateChild
  parent ! CreateChild
  parent ! CreateChild
  parent ! Send
}
