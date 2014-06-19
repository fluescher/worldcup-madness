package com.zuehlke.worldcup.core.model

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import com.github.nscala_time.time.Imports._

case class Game(startTime: String, round: Int,
			    team1: Team, team2: Team, 
			    result: Option[GameResult]) {
  def gameId: String = s"${round}-${team1.abbreviation}-${team2.abbreviation}"
  def tippsAccepted = {
    val format = DateTimeFormat.forPattern("yyyy/MM/dd")
    val startDate = format.parseDateTime(startTime)
    startDate + 18.hours > new DateTime(myZone)
  }
  private val myZone = DateTimeZone.forID("Europe/Zurich")
} 

case class GameResult(goalsTeam1: Int, goalsTeam2: Int)
