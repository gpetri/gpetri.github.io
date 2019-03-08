package io.github.gpetri.ch7

import akka.actor.{Actor, PoisonPill, ReceiveTimeout}
import akka.cluster.Cluster
import akka.cluster.sharding.ShardRegion
import akka.cluster.sharding.ShardRegion.Passivate
import scala.concurrent.duration._

object TemperatureActor {
  case class Location(country: String, city: String) {
    override def toString() = s"$country-$city"
  }
  case class UpdateTemperature(location: Location, curTemp: Double)
  case class GetCurrentTemperature(location: Location)

  val extractEntityId : ShardRegion.ExtractEntityId = {
    case msg@UpdateTemperature(loc, _) => 
      (s"$loc", msg)
    case msg@GetCurrentTemperature(loc) =>
      (s"$loc", msg)
  }
  val numberOfShards = 100
  val extractShardId : ShardRegion.ExtractShardId = {
    case msg@UpdateTemperature(loc, _) => 
      (s"$loc".hashCode % numberOfShards).toString
    case msg@GetCurrentTemperature(loc) =>
      (s"$loc".hashCode % numberOfShards).toString
  }
  val shardName = "Temperature"
}

class TemperatureActor extends Actor {
  import TemperatureActor._

  var loc : Location = Location("", "")
  var tmp : Double = 0.0

  /* var temperatureMap = Map.empty[Location, Double] */
  override def preStart() = {
    println(s"I was created at ${Cluster(context.system).selfUniqueAddress}")
  }
  def receive() = {
    case update @ UpdateTemperature(loc, curTemp) => {
      this.loc = loc
      this.tmp = curTemp
      /* temperatureMap += (loc -> curTemp) */
      println(s"Temp Updated: $loc")
    }
    case GetCurrentTemperature(loc) => 
      sender ! tmp /* temperatureMap(loc) */
  }
}
