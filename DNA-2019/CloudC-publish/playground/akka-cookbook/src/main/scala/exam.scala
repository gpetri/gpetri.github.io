import akka.actor.{ActorSystem, Actor, Props, ActorRef}

case class Add(ar : ActorRef)
case class Message(s : String)

class AActor extends Actor {
  override def receive: Receive = {
    case s : String =>
      println(s"${self.path} received $s")
    case _ => println("error message")
  }
}

class BActor extends Actor {
  var l = List[ActorRef]()

  override def receive: Receive = {
    case Add(ar: ActorRef) =>
      l = l ++ List(ar)
    case Message(m) =>
      l.foreach(_ ! m)
  }
}

object Exam extends App {
  val as = ActorSystem("examActorSystem")
  val b = as.actorOf(Props[BActor], "b")
  b ! Add(as.actorOf(Props[AActor], "a1"))
  b ! Add(as.actorOf(Props[AActor], "a2"))
  b ! Add(as.actorOf(Props[AActor], "a3"))

  b ! Message("Knock knock!")
}
