package com.zuehlke.worldcup.core

import akka.actor.Actor
import akka.actor.Props
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.Groups
import com.zuehlke.worldcup.core.model.Group

class GameManager extends Actor {
  import GameManager._
  
  override def receive = {
    case GetGames => sender ! GetGamesResult(Groups.groupA.games ++
    										 Groups.groupB.games ++
    										 Groups.groupC.games ++ 
    										 Groups.groupD.games) 
    case GetGroups => sender ! GetGroupsResult(List(Groups.groupA,
    												Groups.groupB,
    												Groups.groupC,
    												Groups.groupD))		
  }
}
object GameManager {
  sealed trait GameManagerMessages
  case object GetGames extends GameManagerMessages
  case class GetGamesResult(games: List[Game]) extends GameManagerMessages
  case object GetGroups extends GameManagerMessages
  case class GetGroupsResult(groups: List[Group]) extends GameManagerMessages
  
  def props() = Props[GameManager](new GameManager())
}