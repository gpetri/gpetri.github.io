package io.github.gpetri.ch7

import akka.actor.{ActorRef, Actor, Props}
import akka.cluster.Cluster
import akka.cluster.ddata._
import akka.cluster.ddata.Replicator._

object SubscriptionManager {
  case class Subscription(id: Int, origin: String, creationTS: Long) {
    override def toString : String =
      return s"$id@$origin"
  }
  case class AddSubscription(subs: Subscription)
  case class RemoveSubscription(subs: Subscription)
  case class GetSubscriptions(consistency: ReadConsistency)

  trait GetSubscriptionsResult
  case class GetSubscriptionsSuccess(subscriptions: Set[Subscription]) extends GetSubscriptionsResult
  case object GetSubscriptionsFailure extends GetSubscriptionsResult
  def props = Props(new SubscriptionManager())
  val subscriptionKey = "subscription_key"
}

class SubscriptionManager extends Actor {
  import SubscriptionManager._
  val replicator = DistributedData(context.system).replicator
  implicit val node = Cluster(context.system)
  private val DataKey: ORSetKey[Subscription] = ORSetKey[Subscription](subscriptionKey)
  replicator ! Subscribe(DataKey, self)

  def receive = {
    case AddSubscription(subs: Subscription) =>
      println(s"Adding subscription for $subs")
      replicator ! Update(DataKey, ORSet.empty[Subscription], WriteLocal) (_ + subs)
    case RemoveSubscription(subs: Subscription) =>
      println(s"Removing subscription for $subs")
      replicator ! Update(DataKey, ORSet.empty[Subscription], WriteLocal) (_ - subs)
    case GetSubscriptions(consistency) =>
      replicator ! Get(DataKey, consistency, request = Some(sender()))
    case g @ GetSuccess(DataKey, Some(replyTo: ActorRef)) =>
      val v = g.get(DataKey).elements
      replyTo ! GetSubscriptionsSuccess(v)
    case GetFailure(DataKey, Some(replyTo: ActorRef)) =>
      replyTo ! GetSubscriptionsFailure
    case _ : UpdateResponse[_] =>
    case c @ Changed(DataKey) =>
      val d = c.get(DataKey)
      println(s"Current elements: ${d.elements}")
  }
}
