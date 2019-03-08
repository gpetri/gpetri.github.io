package io.github.gpetri.ch10

import akka.actor.Actor
import io.github.gpetri.ch10.Reaper.WatchMe

trait ReaperAwareActor extends Actor {
  override final def preStart() = {
    registerReaper()
    preStartPostRegistration()
  }

  private def registerReaper() = {
    context.actorSelection("/user/Reaper") ! WatchMe(self)
  }

  def preStartPostRegistration() : Unit = ()
}
