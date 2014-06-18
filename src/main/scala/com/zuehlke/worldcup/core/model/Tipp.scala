package com.zuehlke.worldcup.core.model

case class Tipp(gameId: String, user: User, goalsTeam1: Int, goalsTeam2: Int) {
  def updateUser(newUser: User) = 
    Tipp(gameId, newUser, goalsTeam1, goalsTeam2)
}