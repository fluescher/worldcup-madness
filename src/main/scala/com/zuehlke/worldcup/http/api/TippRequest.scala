package com.zuehlke.worldcup.http.api

import com.zuehlke.worldcup.core.model.Tipp
import com.zuehlke.worldcup.core.model.User

case class TippRequest(gameId: String, goalsTeam1: Int, goalsTeam2: Int) {
  def convert(newUser: User) = 
    Tipp(gameId, newUser, goalsTeam1, goalsTeam2, None)
}