package io.github.gpetri.ch10

import akka.actor.{Actor, ActorLogging}
import io.github.gpetri.ch10.PausableActor.{Ready, Work}

import scala.util.Random

class HardWorker extends Actor with ActorLogging {

  def receive = {
    case Work(id) =>
      Thread.sleep(Random.nextInt(3) * 1000)
      log.info(s"Work [$id] finished.")
      sender ! Ready
  }
}
