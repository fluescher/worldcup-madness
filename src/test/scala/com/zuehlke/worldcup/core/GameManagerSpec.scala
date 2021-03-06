package com.zuehlke.worldcup.core

import org.scalatest.Matchers
import akka.testkit.TestKit
import akka.testkit.ImplicitSender
import org.scalatest.WordSpecLike
import akka.actor.ActorSystem
import GameManager._
import com.zuehlke.worldcup.core.model.Game

class GameManagerSpec extends TestKit(ActorSystem("worldcup-madness")) with WordSpecLike with Matchers  with ImplicitSender { 
  "A game manager" should {
    "return a list of games" in {
      val gameManager = system.actorOf(GameManager.props())

      gameManager ! GamesUpdated(List(Game("", 1, null, null, None)))
      gameManager ! GetGames
     
      val expectedMessage = expectMsgType[GetGamesResult]
      
      expectedMessage.games.length should be(1)
    }
  }
}