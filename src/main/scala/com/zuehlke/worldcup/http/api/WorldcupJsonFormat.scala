package com.zuehlke.worldcup.http.api

import spray.json._
import DefaultJsonProtocol._
import spray.httpx.SprayJsonSupport._
import com.zuehlke.worldcup.core.model.Team
import com.zuehlke.worldcup.core.model.User
import com.zuehlke.worldcup.core.model.GameResult
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.GameManager._
import com.zuehlke.worldcup.core.model.Ranking
import com.zuehlke.worldcup.core.model.Tipp

object WorldcupJsonFormat {
  implicit val teamFormat = jsonFormat(Team, "name", "abbrevation")
  implicit object UserFormat extends RootJsonFormat[User] {
    def write(u: User) = JsObject(
      "name" -> JsString(u.name),
      "email" -> JsString(u.email ),
      "forename" -> JsString(u.forename ),
      "surname" -> JsString(u.surname)
    )
    def read(value: JsValue): User = {
      value.asJsObject.getFields("name", "email", "password", "forename", "surname") match {
        case Seq(JsString(name), JsString(email), JsString(password), JsString(forename), JsString(surname)) =>
          new User(name, password, forename, surname, email)
        case _ => throw new DeserializationException(s"User expected, but got: $value")
      }
    } 
  }
  implicit val gameResultFormat = jsonFormat2(GameResult)
  implicit object GameFormat extends RootJsonFormat[Game] {
    def write(g: Game) = JsObject(
      "startTime" -> JsString(g.startTime),
      "round" -> JsNumber(g.round),
      "team1" -> teamFormat.write(g.team1),
      "team2" -> teamFormat.write(g.team2),
      "result" -> (g.result match {
        case None 			=> JsNull
        case Some(result) 	=> gameResultFormat.write(result)
      }),
      "gameId" -> JsString(g.gameId)
    )
    def read(value: JsValue): Game = ??? /* We never have to read a Game */
  }
  implicit val rankingFormat = jsonFormat2(Ranking.apply)
  implicit val tippRequestFormat = jsonFormat3(TippRequest.apply)
  implicit val tippResponseFormat = jsonFormat3(TippResponse.apply)

}