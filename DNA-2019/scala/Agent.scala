import akka.agent.Agent
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object AgentApp extends App {
  val to = Timeout(10 seconds)
  val ag = Agent(5)
  val res = ag.get
  println(s"Result is now $res")
  val f1 : Future[Int] = ag alter 7
  println(s"Result after sending 7 ${Await.result(f1, 10 seconds)}")
  val f2 : Future[Int] = ag alter (_ + 3)
  println(s"Result after sending a function ${Await.result(f2, 10 seconds)}")
}
