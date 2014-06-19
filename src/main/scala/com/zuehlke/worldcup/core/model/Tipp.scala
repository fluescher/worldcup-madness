package com.zuehlke.worldcup.core.model

import com.zuehlke.worldcup.http.api.TippResponse

case class Tipp(gameId: String, user: User, goalsTeam1: Int, goalsTeam2: Int, tippResult: Option[TippResult]) {
  
  def updateUser(newUser: User) = 
    Tipp(gameId, newUser, goalsTeam1, goalsTeam2, None)
    
  def convert = 
    TippResponse(gameId, goalsTeam1, goalsTeam2)

}