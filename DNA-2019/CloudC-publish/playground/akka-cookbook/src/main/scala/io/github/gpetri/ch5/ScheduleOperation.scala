package io.github.gpetri.ch5

import akka.actor.ActorSystem
import scala.concurrent.duration._

object ScheduleOperationApp extends App {
  val as = ActorSystem("Scheduler")
  import as.dispatcher
  as.scheduler.scheduleOnce(10 seconds) {
    println(s"Sum of (1 + 2) is ${1 + 2}")
  }
  as.scheduler.schedule(11 seconds, 2 seconds) {
    println("Annoying message\n")
  }
}
