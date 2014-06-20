package com.zuehlke.worldcup.core.model

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import com.github.nscala_time.time.Imports._

case class Game(startTime: String, round: Int,
			    team1: Team, team2: Team, 
			    result: Option[GameResult]) {
  def gameId: String = s"${round}-${team1.abbreviation}-${team2.abbreviation}"
  
  def tippsAccepted(dateTime: DateTime): Boolean = {
    val format = DateTimeFormat.forPattern("yyyy/MM/dd").withZone(myZone)
    val startDate = format.parseDateTime(startTime)
    println(s"$this: comparing $dateTime with ${startDate.withZone(myZone) + 18.hours}")
    startDate + 18.hours > dateTime
  }
  
  def tippsAccepted: Boolean = {
    tippsAccepted(new DateTime(myZone))
  }
  private val myZone = DateTimeZone.forID("Europe/Zurich")
} 

case class GameResult(goalsTeam1: Int, goalsTeam2: Int)
