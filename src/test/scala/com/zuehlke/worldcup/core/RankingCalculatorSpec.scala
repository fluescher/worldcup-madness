package com.zuehlke.worldcup.core

import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import com.zuehlke.worldcup.core.model.Tipp
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.GameResult

class RankingCalculatorSpec extends WordSpecLike with Matchers {
  "A ranking calculator" should {
    "be able to calculate the winning score" in {
      val calculator = new RankingCalculator()
      
      val points = calculator.calculateWinningPoints(getTippWithResult(1,2), getGameWithResult(1,2))
      
      points should be(5)
    }
    "be able to calculate the winning score when the tipp is wrong" in {
      val calculator = new RankingCalculator()
      
      val points = calculator.calculateWinningPoints(getTippWithResult(1,0), getGameWithResult(1,2))
      
      points should be(0)
    }
    "be able to calculate the winning score when the tipp is draw" in {
      val calculator = new RankingCalculator()
      
      val points = calculator.calculateWinningPoints(getTippWithResult(1,1), getGameWithResult(1,2))
      
      points should be(0)
    }
    "be able to calculate the goal difference score when the tipp is draw" in {
      val calculator = new RankingCalculator()
      
      val points = calculator.calculateGoalDifferencePoints(getTippWithResult(1,1), getGameWithResult(1,2))
      
      points should be(0)
    }
    "be able to calculate the goal difference score when the tipp is correct" in {
      val calculator = new RankingCalculator()
      
      val points = calculator.calculateGoalDifferencePoints(getTippWithResult(2,1), getGameWithResult(1,2))
      
      points should be(1)
    }
    "be able to calculate the goal difference score when the tipp is absolutly correct" in {
      val calculator = new RankingCalculator()
      
      val points = calculator.calculateGoalDifferencePoints(getTippWithResult(1,2), getGameWithResult(1,2))
      
      points should be(1)
    }
    "be able to calculate the exact goal tipp score when one goal tipp is correct" in {
      val calculator = new RankingCalculator()
      
      val points = calculator.calculateExactGoalMatchPoints(getTippWithResult(0,2), getGameWithResult(1,2))
      
      points should be(2)
    }
    "be able to calculate the exact goal tipp score when the other goal tipp is correct" in {
      val calculator = new RankingCalculator()
      
      val points = calculator.calculateExactGoalMatchPoints(getTippWithResult(1,0), getGameWithResult(1,2))
      
      points should be(2)
    }
    "be able to calculate the exact goal tipp score when both goal tipp is correct" in {
      val calculator = new RankingCalculator()
      
      val points = calculator.calculateExactGoalMatchPoints(getTippWithResult(1,2), getGameWithResult(1,2))
      
      points should be(4)
    }
  }
  def getTippWithResult(teamA: Int, teamB: Int) = Tipp("", null, teamA, teamB)
  def getGameWithResult(teamA: Int, teamB: Int) = Game(null, 0, null, null, Some(GameResult(teamA, teamB)))
}