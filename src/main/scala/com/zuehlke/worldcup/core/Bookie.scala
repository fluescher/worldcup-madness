package com.zuehlke.worldcup.core

import akka.persistence.{ Persistent, PersistenceFailure, Processor }
import akka.actor.Props

class Bookie extends Processor {
  def receive = {
	case Persistent(payload, sequenceNr) => 
	  println("got journaled: " + payload)
	  sender ! payload
	case PersistenceFailure(payload, sequenceNr, cause) =>
	  println("unable to journal: " + payload)
	case payload => 
	  println("unjournaled: " + payload)
  }
}

object Bookie {
  sealed trait BookieMessage
  case object PlaceBet extends BookieMessage

  def props(): Props =
    Props(new Bookie())
}