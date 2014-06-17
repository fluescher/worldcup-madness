package com.zuehlke.worldcup.core

import com.zuehlke.worldcup.core.model.User
import com.zuehlke.worldcup.core.model.Ranking
import scala.collection.immutable.Set

class RankingCalculator {
  
  def calculate(users: List[User]) : List[Ranking] = {
	  List(Ranking("pas", 12), Ranking("fllu", 15)).sortBy(_.points)
  }
  
}