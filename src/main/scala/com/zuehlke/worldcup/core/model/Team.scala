package com.zuehlke.worldcup.core.model

case class Team(name: String, flag: String, abbreviation: String)

object Team {
  val switzerland = Team("Schweiz", "", "CH")
  val honduras = Team("Honduras", "", "HON")
  val france = Team("Frankreich", "", "FRA")
  val ecuador = Team("Ecuador", "", "ECU")
}