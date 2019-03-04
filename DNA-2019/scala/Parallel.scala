import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

object Fib {
  def fib(n : Long) = {
    def tail_fib(n: Long, a: Long, b: Long) : Long = n match {
      case 0 => a
      case _ => tail_fib(n - 1, b, a + b)
    }
    tail_fib(n, 0, 1)
  }
}

object ParallelApp extends App {
  import Fib._
  val t1 = System.currentTimeMillis
  var sum = fib(100L)
  sum = sum + fib(100L)
  sum = sum + fib(100L)
  println(s"sum is $sum and it took ${System.currentTimeMillis - t1} millis")
  val t2 = System.currentTimeMillis
  val f1 = Future(fib(100L))
  val f2 = Future(fib(100L))
  val f3 = Future(fib(100L))
  val f = for {
    x <- f1
    y <- f2
    z <- f3
  } yield (x + y + z)

  f onSuccess {
    case sum => 
      val et = System.currentTimeMillis - t2
      println(s"parallel sum is $sum and it took $et millis")
  }
  Thread.sleep(5000)
}
