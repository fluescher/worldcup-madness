package com.zuehlke.worldcup.core

import com.zuehlke.worldcup.core.model.User
import com.zuehlke.worldcup.core.model.Ranking
import scala.collection.immutable.Set
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.Tipp

class RankingCalculator {
  
  def calculateRanking(tipps: List[Tipp], games: List[Game]): List[Ranking] = 
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
}