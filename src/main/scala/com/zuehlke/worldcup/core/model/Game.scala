package com.zuehlke.worldcup.core.model

case class Game(startTime: String, team1: Team, team2: Team, result: Option[GameResult]) 

case class GameResult(goalsTeam1: Int, goalsTeam2: Int)