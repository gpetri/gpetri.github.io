import akka.agent.Agent
import scala.concurrent.ExecutionContext.Implicits.global

object AgentCompositionApp extends App {
  val ag1 = Agent("Hello, ")
  val ag2 = Agent("World")
  val fagent = for {
    x <- ag1
    y <- ag2
  } yield (x + y)
  println(s"Final agent result is ${fagent.get}")
}
