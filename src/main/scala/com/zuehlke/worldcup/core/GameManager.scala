package com.zuehlke.worldcup.core

import akka.actor.Actor
import akka.actor.Props
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.Team
import akka.actor.ActorLogging


class GameManager extends Actor with ActorLogging {
  import GameManager._
  import context._
  
  implicit val system = context.system
  
  var games: List[Game] = Nil
  
  override def receive = {
    case GetGames => sender ! GetGamesResult(games)
    
    case GamesUpdated(gs) =>
      log.info(s"Got ${gs} games")
      games = gs
  }
  
}
object GameManager {
  sealed trait GameManagerMessages
  case object GetGames extends GameManagerMessages
  case class GetGamesResult(games: List[Game]) extends GameManagerMessages
  case class GamesUpdated(teams: List[Game]) extends GameManagerMessages
  
  def props() = Props(new GameManager())
}