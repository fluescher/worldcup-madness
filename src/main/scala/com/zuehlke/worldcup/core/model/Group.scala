package com.zuehlke.worldcup.core.model

import java.util.Date

case class Group(name: String, games: List[Game]) 

object Groups {
  import Team._
  
  val groupA = Group("A", List(	Game(new Date().toString(), switzerland, ecuador, None),
		  						Game(new Date().toString(), france, honduras, None),
		  						Game(new Date().toString(), switzerland, france, None),
		  						Game(new Date().toString(), ecuador,honduras, None),
		  						Game(new Date().toString(), honduras, switzerland, None),
		  						Game(new Date().toString(), france, ecuador, None)))
  
  val groupB = Group("B", List())
  
  val groupC = Group("C", List())
  
  val groupD = Group("D", List())
}