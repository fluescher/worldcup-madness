package com.zuehlke.worldcup.core

import akka.persistence.{ Persistent, PersistenceFailure, Processor }
import akka.actor.Props
import com.zuehlke.worldcup.core.model.Tipp
import akka.actor.ActorLogging
import com.zuehlke.worldcup.core.model.User
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.Ranking

class Bookie extends Processor with ActorLogging {
  
  private var tipps: List[Tipp] = Nil
  
  import Bookie._
  
  override def receive = {
	case Persistent(PlaceBet(tipp), sequenceNr) => 
	  placeBet(tipp)
	  sender ! BetPlaced
	case PersistenceFailure(payload, sequenceNr, cause) =>
	  log.error(s"Unable to persist message $payload")
	  
	case GetAllBets => 
	  sender ! GetAllBetsResult(tipps.groupBy(_.user.name))
	
	case GetBets(username) =>
	  sender ! GetBetsResult(tipps.filter(_.user.name == username))
	  
	case CalculatePoints(games) =>
	  sender ! RankingResult(calculateRanking(tipps, games).sortBy(_.points))
  }
  
  private def calculateRanking(tipps: List[Tipp], games: List[Game]): List[Ranking] = 
    tipps.map(tipp => games.find(_.gameId == tipp.gameId) match {
      case None 		=> (tipp, 0)
      case Some(game) 	if game.result.isDefined => (tipp, calculatePoints(tipp, game))
    }).groupBy(_._1).values.flatten.map(toRanking(_)).toList
    
  def calculatePoints(tipp: Tipp, game: Game): Int =
    calculateWinningPoints(tipp, game) +
    calculateGoalDifferencePoints(tipp, game) +
    calculateExactGoalMatchPoints(tipp, game)
    
  def calculateWinningPoints(tipp: Tipp, game: Game): Int = {
    val tippDiff = tipp.goalsTeam1 - tipp.goalsTeam2
    val gameDiff = game.result.get.goalsTeam1 - game.result.get.goalsTeam2
    
    if (tippDiff > 0 && gameDiff > 0 || tippDiff < 0 && gameDiff < 0 || tippDiff == 0 && gameDiff == 0) {
      5
    } else {
      0
    }
  }
    
  def calculateGoalDifferencePoints(tipp: Tipp, game: Game): Int = {
    val tippDiff = tipp.goalsTeam1 - tipp.goalsTeam2
    val gameDiff = game.result.get.goalsTeam1 - game.result.get.goalsTeam2
    
    math.abs(tippDiff) == math.abs(gameDiff) match {
      case true 	=> 1
      case false 	=> 0
    }
  }
  
  def calculateExactGoalMatchPoints(tipp: Tipp, game: Game): Int = {
    if(tipp.goalsTeam1 == game.result.get.goalsTeam1 && tipp.goalsTeam2 == game.result.get.goalsTeam2) {
      4
    } else if(tipp.goalsTeam1 == game.result.get.goalsTeam1 || tipp.goalsTeam2 == game.result.get.goalsTeam2) {
      2
    } else {
      0
    }
  }
   
  private def toRanking(tippScore : (Tipp, Int)): Ranking = tippScore match {
    case (tipp, score) => Ranking(tipp.user.name, score)
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
  case class CalculatePoints(games: List[Game]) extends BookieMessage
  case class RankingResult(rankings: List[Ranking]) extends BookieMessage
  
  def props(): Props =
    Props(new Bookie())
}