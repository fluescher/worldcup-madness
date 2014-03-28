package com.zuehlke.worldcup.core.model

import org.scalatest._

class GameTest extends FlatSpec with Matchers {
  "A Game" should "have a string representation" in {
    val game = new Game("a")
    game.toString() should be("a")
  }
}