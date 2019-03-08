package io.github.gpetri.ch10

import akka.actor.{Actor, ActorLogging}
import io.github.gpetri.ch10.TrafficLightFSM.ReportChange

class FSMChangeSubscriber extends Actor with ActorLogging {
  def receive = { case ReportChange(s, d) => log.info(s"Change detected to [$s] at [$d]") }
}
