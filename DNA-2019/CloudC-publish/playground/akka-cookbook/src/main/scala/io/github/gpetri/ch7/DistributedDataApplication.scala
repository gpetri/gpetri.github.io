package io.github.gpetri.ch7

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.cluster.ddata.Replicator.{ReadFrom, ReadMajority, ReadLocal}
import akka.pattern.ask
import akka.util.Timeout
import io.github.gpetri.ch7.SubscriptionManager._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random

object DistributedDataApplication extends App {
  val as = ActorSystem("ClusterSystem")
  Cluster(as).registerOnMemberUp {
    val subsManager = as.actorOf(SubscriptionManager.props)
    val subs = Subscription(
      Random.nextInt(10), Cluster(as).selfUniqueAddress.address.port.getOrElse(0).toString , System.currentTimeMillis()
    )
    subsManager ! AddSubscription(subs)

    Thread.sleep(10000)
    implicit val to = Timeout(5 seconds)
    val readMajority = ReadMajority(timeout = 5 seconds)
    /* val readFrom = ReadFrom(n = 2, timeout = 5 seconds) */
    Await.result(subsManager ? GetSubscriptions(ReadLocal), 5 seconds) match {
      case GetSubscriptionsSuccess(subs) =>
        println(s"The current subscriptions are $subs")
      case GetSubscriptionsFailure =>
        println(s"Subs manager could not get subscriptions")
    }
    subsManager ! RemoveSubscription(subs)
    Await.result(subsManager ? GetSubscriptions(ReadLocal), 5 seconds) match {
      case GetSubscriptionsSuccess(subs) =>
        println(s"The current subscriptions are $subs")
      case GetSubscriptionsFailure =>
        println(s"Subs manager could not get subscriptions")
    }
  }
}
