package com.zuehlke.worldcup.core

import com.zuehlke.worldcup.core.model.User
import com.zuehlke.worldcup.core.model.Ranking
import scala.collection.immutable.Set
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.Tipp
import com.zuehlke.worldcup.core.model.TippResult

class RankingCalculator {
  
  def calculateTippResults(tipps: List[Tipp], games: List[Game]): List[Tipp] =
    tipps.map(tipp => games.find(_.gameId == tipp.gameId) match {
      case Some(game) 	if game.result.isDefined => Tipp(tipp.gameId, tipp.user, tipp.goalsTeam1 , tipp.goalsTeam2, 
    		  										     Some(TippResult(
    		  										          calculateWinningPoints(tipp, game),
    		  										          calculateGoalDifferencePoints(tipp, game),
    		  										          calculateExactGoalMatchPoints(tipp, game),
    		  										          calculatePoints(tipp, game))))
     case _ 		=> tipp
    })
  
  def calculateRanking(tipps: List[Tipp], games: List[Game]): List[Ranking] = 
    calculateTippResults(tipps, games).groupBy(_.user.name)
    								  .mapValues(_.filter(_.tippResult == Some).map(_.tippResult.get.totalPoint).sum)
    								  .map({case (username, points) => Ranking(username, points)}).toList
    
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
  
//  private def toRanking(tippScore : Tipp): Ranking = 
//    Ranking(tipp.user.name, score)
}