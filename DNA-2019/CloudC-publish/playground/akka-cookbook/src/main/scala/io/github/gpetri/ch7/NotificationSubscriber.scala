package io.github.gpetri.ch7

import akka.actor.Actor
import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Subscribe, SubscribeAck}

case class Notification(title: String, body: String)

class NotificationSubscriber extends Actor {
  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe("notification", self)
  val cluster = Cluster(context.system)
  val clusterAddr = cluster.selfUniqueAddress

  def receive = {
    case notification : Notification => 
      println(s"Got notification in node $clusterAddr => $notification")
    case SubscribeAck(Subscribe("notification", None, self)) =>
      println("subscribing")
  }
}
