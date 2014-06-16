package com.zuehlke.worldcup.core

import akka.persistence.{ Persistent, PersistenceFailure, Processor }
import akka.actor.Props
import com.zuehlke.worldcup.core.model.Tipp

class Bookie extends Processor {
  
  private var tipps: List[Tipp] = Nil
  
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
  case class PlaceBet(tipp: Tipp) extends BookieMessage

  def props(): Props =
    Props(new Bookie())
}