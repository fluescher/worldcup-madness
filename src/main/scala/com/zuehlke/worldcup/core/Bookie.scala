package com.zuehlke.worldcup.core

import akka.persistence.{ Persistent, PersistenceFailure, Processor }
import akka.actor.Props
import com.zuehlke.worldcup.core.model.Tipp
import akka.actor.ActorLogging
import com.zuehlke.worldcup.core.model.User

class Bookie extends Processor with ActorLogging {
  
  private var tipps: List[Tipp] = Nil
  
  import Bookie._
  
  override def receive = {
	case Persistent(PlaceBet(tipp), sequenceNr) => 
	  placeBet(tipp)
	  sender ! BetPlaced
	case PersistenceFailure(payload, sequenceNr, cause) =>
	  log.error(s"Unable to persist message $payload")
	  
	case GetAllBets => sender ! GetAllBetsResult(tipps.groupBy(_.user.name))
	
	case GetBets(username) =>
	  sender ! GetBetsResult(tipps.filter(_.user.name == username))
  }
  
  private def placeBet(newTipp: Tipp) = 
    tipps = newTipp :: tipps.filterNot(tipp => 
      tipp.gameId == newTipp.gameId && tipp.user.name == newTipp.user.name )
    
}

object Bookie {
  sealed trait BookieMessage
  case class PlaceBet(tipp: Tipp) extends BookieMessage
  case object BetPlaced extends BookieMessage
  case object BetInvalid extends BookieMessage
  case object GetAllBets extends BookieMessage
  case class GetBets(user: String) extends BookieMessage
  case class GetBetsResult(bets: List[Tipp]) extends BookieMessage
  case class GetAllBetsResult(bets: Map[String, List[Tipp]]) extends BookieMessage
  
  def props(): Props =
    Props(new Bookie())
}