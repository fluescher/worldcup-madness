package com.zuehlke.worldcup.core

import akka.persistence.{ Persistent, PersistenceFailure, Processor }
import akka.actor.Props
import com.zuehlke.worldcup.core.model.Tipp
import akka.actor.ActorLogging
import com.zuehlke.worldcup.core.model.User
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.Ranking
import akka.actor.ActorRef
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
import akka.actor.ActorSystem
import org.joda.time.DateTime

class Bookie(val gameManager: ActorRef)(implicit system: ActorSystem) extends Processor with ActorLogging {
  
  implicit val executionContext = system.dispatcher
  implicit val defaultTimeout = Timeout(5.seconds)
  val calculator = new RankingCalculator()
  
  private var tipps: List[Tipp] = Nil
  
  import Bookie._
  
  override def receive = {
	case Persistent(PlaceBet(tipp, time), sequenceNr) =>
	  val respondTo = sender
	  import GameManager._
	  (gameManager ? GetGames).mapTo[GetGamesResult].map(_.games ).map(games => {
	    games.find(_.gameId == tipp.gameId) match {
	      case None 									=> respondTo ! BetInvalid
	      case Some(game) if !game.tippsAccepted(time)	=> respondTo ! BetInvalid
	      case Some(game)								=> self ! UpdateBets(tipp, respondTo)
	    }
	  })

	case UpdateBets(tipp, respondTo) => 
	  placeBet(tipp)
	  respondTo ! BetPlaced
	  
	case PersistenceFailure(payload, sequenceNr, cause) =>
	  log.error(s"Unable to persist message $payload")
	  
	case GetAllBets => 
	  sender ! GetAllBetsResult(tipps.groupBy(_.user.name))
	
	case GetBets(username) =>
	  sender ! GetBetsResult(tipps.filter(_.user.name == username))
	  
	case CalculatePoints(games) =>
	  sender ! RankingResult(calculator.calculateRanking(tipps, games).sortBy(_.points))
  }
  
  private def placeBet(newTipp: Tipp) = 
    tipps = newTipp :: tipps.filterNot(tipp => 
      tipp.gameId == newTipp.gameId && tipp.user.name == newTipp.user.name )
  
}

object Bookie {
  sealed trait BookieMessage
  case class PlaceBet(tipp: Tipp, time: DateTime) extends BookieMessage
  case class UpdateBets(tipp: Tipp, respondTo: ActorRef) extends BookieMessage
  case object BetPlaced extends BookieMessage
  case object BetInvalid extends BookieMessage
  case object GetAllBets extends BookieMessage
  case class GetBets(user: String) extends BookieMessage
  case class GetBetsResult(bets: List[Tipp]) extends BookieMessage
  case class GetAllBetsResult(bets: Map[String, List[Tipp]]) extends BookieMessage
  case class CalculatePoints(games: List[Game]) extends BookieMessage
  case class RankingResult(rankings: List[Ranking]) extends BookieMessage
  
  def props(gameManager: ActorRef)(implicit system: ActorSystem): Props =
    Props(new Bookie(gameManager))
}