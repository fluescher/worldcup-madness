package com.zuehlke.worldcup.core.model

import org.scalatest._
import org.joda.time.DateTimeZone
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import com.github.nscala_time.time.Imports._

class GameSpec extends FlatSpec with Matchers {
  "A Game" should "have a string representation" in {
    val myZone = DateTimeZone.forID("Europe/Zurich")
    val zurichTime = new DateTime(myZone)
    
    val format = DateTimeFormat.forPattern("yyyy/MM/dd")
    val whatever = format.parseDateTime("2014/06/20").withZone(myZone) + 18.hours
    println(s"zurich: $zurichTime")
    println(s"zurich: $whatever")
    println(zurichTime < whatever )
  }
}