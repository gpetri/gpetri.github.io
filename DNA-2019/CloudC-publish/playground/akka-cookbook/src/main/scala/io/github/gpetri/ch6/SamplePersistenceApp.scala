package io.github.gpetri.ch6

import akka.actor.{ActorSystem, Props}

object SamplePersistenceApp extends App {
  val as = ActorSystem("PersistenceSystem")
  val persistent1 = as.actorOf(Props[SamplePersitenceActor])

  persistent1 ! UserUpdate("foo", Add)
  persistent1 ! UserUpdate("baz", Add)
  persistent1 ! "snap"
  persistent1 ! "print"
  persistent1 ! UserUpdate("baz", Remove)
  persistent1 ! "print"
  Thread.sleep(2000)
  as.stop(persistent1)
  val persistent2 = as.actorOf(Props[SamplePersitenceActor])
  persistent2 ! "print"
  Thread.sleep(2000)
  as.terminate()
}
