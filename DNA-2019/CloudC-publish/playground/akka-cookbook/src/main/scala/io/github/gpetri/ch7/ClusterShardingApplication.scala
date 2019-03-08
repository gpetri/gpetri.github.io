package io.github.gpetri.ch7

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import io.github.gpetri.ch7.TemperatureActor.{GetCurrentTemperature, Location, UpdateTemperature}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.util.Random

object ClusterShardingApplication extends App {
  val as = ActorSystem("ClusterSystem")
  import as.dispatcher

  val temperatureActor: ActorRef =
    ClusterSharding(as).start (
      typeName = TemperatureActor.shardName,
      entityProps = Props[TemperatureActor],
      settings = ClusterShardingSettings(as),
      extractEntityId = TemperatureActor.extractEntityId,
      extractShardId = TemperatureActor.extractShardId
    )
  Thread.sleep(30000)
  val locations = Vector(
    Location("MA", "Marrakesh"),
    Location("US", "Chicago"),
    Location("ES", "Madrid"),
    Location("AR", "Cordoba"),
    Location("FR", "Paris"),
    Location("IT", "Rome"),
    Location("UK", "London"),
    Location("CH", "Zhurich"),
    Location("PR", "Lisbon"),
    Location("RU", "Moscow")
  )
  locations.foreach {
    case loc =>
      temperatureActor ! UpdateTemperature(loc, Random.nextDouble())
  }


  /* temperatureActor ! UpdateTemperature(locations(0), 1.0)
   * temperatureActor ! UpdateTemperature(locations(1), 20.0)
   * temperatureActor ! UpdateTemperature(locations(2), 25.0) */

  implicit val to : Timeout = Timeout(5 seconds)
  locations.foreach {
    case loc =>
      (temperatureActor ? GetCurrentTemperature(loc)).onSuccess {
        case x : Double =>
          println(s"Current temp in $loc is $x")
      }
  }
}
