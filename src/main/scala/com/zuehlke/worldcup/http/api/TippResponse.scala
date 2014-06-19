package com.zuehlke.worldcup.http.api

import com.zuehlke.worldcup.core.model.Tipp
import com.zuehlke.worldcup.core.model.User
import com.zuehlke.worldcup.core.model.TippResult

case class TippResponse(gameId: String, goalsTeam1: Int, goalsTeam2: Int, tippResult: Option[TippResult]) 
//case class TippResponse(gameId: String, goalsTeam1: Int, goalsTeam2: Int) 