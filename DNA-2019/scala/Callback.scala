package io.github.gpetri.ch4

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Callback extends App {
  val fut = Future(1 + 2).mapTo[Int]
  fut onComplete {
    case Success(res) => println(s"result $res")
    case Failure(fail) => fail.printStackTrace()
  }
  println("Before callback")
}
