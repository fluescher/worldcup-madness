package com.zuehlke.worldcup.http.api

import spray.json._
import DefaultJsonProtocol._
import spray.httpx.SprayJsonSupport._
import com.zuehlke.worldcup.core.model.Team
import com.zuehlke.worldcup.core.model.User
import com.zuehlke.worldcup.core.model.GameResult
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.Group
import com.zuehlke.worldcup.core.GameManager._

object WorldcupJsonFormat {
 implicit val teamFormat = jsonFormat3(Team.apply)
  implicit val userFormat = jsonFormat2(User.apply)
  implicit val gameResultFormat = jsonFormat2(GameResult)
  implicit val gameFormat = jsonFormat4(Game.apply)
  implicit val groupFormat = jsonFormat2(Group.apply)

  implicit val getGroupsResultFormat = jsonFormat1(GetGroupsResult)
  implicit val getGamesResultFormat = jsonFormat1(GetGamesResult)
}