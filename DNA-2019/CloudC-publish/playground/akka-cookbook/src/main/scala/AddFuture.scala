import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
object AddFutureApp extends App {


  val f = Future( 1 + 2 ).mapTo[Int]
  println(f)

  val r = Await.result(f, 2 seconds)
  println(s"the result is $r")
}
