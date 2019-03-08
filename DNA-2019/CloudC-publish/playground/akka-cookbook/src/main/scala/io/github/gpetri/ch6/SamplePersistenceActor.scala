package io.github.gpetri.ch6

import akka.persistence.{ PersistentActor, SnapshotOffer }

class SamplePersitenceActor extends PersistentActor {
  override val persistenceId = "unique-id-1"
  var state = ActiveUsers()
  def updateState(event: Event) = state = state.update(event)

  val receiveRecover: Receive = {
    case evt: Event => updateState(evt)
    case SnapshotOffer(_, snapshot: ActiveUsers) => state = snapshot
  }

  val receiveCommand: Receive = {
    case UserUpdate(userId, Add) =>
      persist(AddUserEvent(userId))(updateState)
    case UserUpdate(userId, Remove) =>
      persist(RemoveUserEvent(userId))(updateState)
    case "snap" => saveSnapshot(state)
    case "print" => println(state)
  }
}
