package io.github.gpetri.ch7

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import scala.concurrent.duration._
import scala.util.Random

object DistributedPubSubApplication extends App {
  val as = ActorSystem("ClusterSystem")
  val cluster = Cluster(as)
  val notificationSubscriber = as.actorOf(Props[NotificationSubscriber])
  val notificationPublisher = as.actorOf(Props[NotificationPublisher])
  val clusterAddress = cluster.selfUniqueAddress
  val notification = Notification(s"Sent from $clusterAddress", "Test!")

  import as.dispatcher
  as.scheduler.schedule(Random.nextInt(5) seconds, 5 seconds, notificationPublisher, notification)
}

/* Subscriber only -- not tested in Demos.org */
object DistributedPubSubApplicationSubs extends App {
  val as = ActorSystem("ClusterSystem")
  val cluster = Cluster(as)
  val notificationSubscriber = as.actorOf(Props[NotificationSubscriber])
}
