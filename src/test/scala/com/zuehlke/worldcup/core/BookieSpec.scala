package com.zuehlke.worldcup.core

import org.scalatest.Matchers
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.actor.ActorSystem
import org.scalatest.WordSpecLike
import scala.concurrent.duration._
import akka.testkit.ImplicitSender
import akka.persistence.Persistent
import com.zuehlke.worldcup.core.model.GameResult
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.Tipp

class BookieSpec extends TestKit(ActorSystem("worldcup-madness")) with WordSpecLike with Matchers with ImplicitSender {
  "A bookie" should {
    "be able to calculate the winning score" in {
      val bookieRef = TestActorRef[Bookie]
      val bookie = bookieRef.underlyingActor
      
      val points = bookie.calculateWinningPoints(getTippWithResult(1,2), getGameWithResult(1,2))
      
      points should be(5)
    }
    "be able to calculate the winning score when the tipp is wrong" in {
      val bookieRef = TestActorRef[Bookie]
      val bookie = bookieRef.underlyingActor
      
      val points = bookie.calculateWinningPoints(getTippWithResult(1,0), getGameWithResult(1,2))
      
      points should be(0)
    }
    "be able to calculate the winning score when the tipp is draw" in {
      val bookieRef = TestActorRef[Bookie]
      val bookie = bookieRef.underlyingActor
      
      val points = bookie.calculateWinningPoints(getTippWithResult(1,1), getGameWithResult(1,2))
      
      points should be(0)
    }
    "be able to calculate the goal difference score when the tipp is draw" in {
      val bookieRef = TestActorRef[Bookie]
      val bookie = bookieRef.underlyingActor
      
      val points = bookie.calculateGoalDifferencePoints(getTippWithResult(1,1), getGameWithResult(1,2))
      
      points should be(0)
    }
    "be able to calculate the goal difference score when the tipp is correct" in {
      val bookieRef = TestActorRef[Bookie]
      val bookie = bookieRef.underlyingActor
      
      val points = bookie.calculateGoalDifferencePoints(getTippWithResult(2,1), getGameWithResult(1,2))
      
      points should be(1)
    }
    "be able to calculate the goal difference score when the tipp is absolutly correct" in {
      val bookieRef = TestActorRef[Bookie]
      val bookie = bookieRef.underlyingActor
      
      val points = bookie.calculateGoalDifferencePoints(getTippWithResult(1,2), getGameWithResult(1,2))
      
      points should be(1)
    }
    "be able to calculate the exact goal tipp score when one goal tipp is correct" in {
      val bookieRef = TestActorRef[Bookie]
      val bookie = bookieRef.underlyingActor
      
      val points = bookie.calculateExactGoalMatchPoints(getTippWithResult(0,2), getGameWithResult(1,2))
      
      points should be(2)
    }
    "be able to calculate the exact goal tipp score when the other goal tipp is correct" in {
      val bookieRef = TestActorRef[Bookie]
      val bookie = bookieRef.underlyingActor
      
      val points = bookie.calculateExactGoalMatchPoints(getTippWithResult(1,0), getGameWithResult(1,2))
      
      points should be(2)
    }
    "be able to calculate the exact goal tipp score when both goal tipp is correct" in {
      val bookieRef = TestActorRef[Bookie]
      val bookie = bookieRef.underlyingActor
      
      val points = bookie.calculateExactGoalMatchPoints(getTippWithResult(1,2), getGameWithResult(1,2))
      
      points should be(4)
    }
  }
  def getTippWithResult(teamA: Int, teamB: Int) = Tipp("", null, teamA, teamB)
  def getGameWithResult(teamA: Int, teamB: Int) = Game(null, 0, null, null, Some(GameResult(teamA, teamB)))
}