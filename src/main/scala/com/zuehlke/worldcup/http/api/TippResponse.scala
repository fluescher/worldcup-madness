package com.zuehlke.worldcup.http.api

import com.zuehlke.worldcup.core.model.Tipp
import com.zuehlke.worldcup.core.model.User

case class TippResponse(gameId: String, goalsTeam1: Int, goalsTeam2: Int) 