package com.zuehlke.worldcup.core.model

case class Game(startTime: String, round: Int,
			    team1: Team, team2: Team, 
			    result: Option[GameResult]) {
  def gameId: String = s"${round}-${team1.abbreviation}-${team2.abbreviation}"
} 

case class GameResult(goalsTeam1: Int, goalsTeam2: Int)
