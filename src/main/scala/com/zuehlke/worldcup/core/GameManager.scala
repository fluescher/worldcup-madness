package com.zuehlke.worldcup.core

import akka.actor.Actor
import akka.actor.Props
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.Groups

class GameManager extends Actor {
  import GameManager._
  
  override def receive = {
    case GetGames => sender ! GetGamesResult(Groups.groupA.games) 
  }
}
object GameManager {
  sealed trait GameManagerMessages
  case object GetGames extends GameManagerMessages
  case class GetGamesResult(games: List[Game]) extends GameManagerMessages
  
  def props() = Props[GameManager](new GameManager())
}