package com.zuehlke.worldcup.core

import org.scalatest.Matchers
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.actor.ActorSystem
import org.scalatest.WordSpecLike
import scala.concurrent.duration._
import akka.testkit.ImplicitSender
import akka.persistence.Persistent
import com.zuehlke.worldcup.core.model.GameResult
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.Tipp

class BookieSpec extends TestKit(ActorSystem("worldcup-madness")) with WordSpecLike with Matchers with ImplicitSender {
  "A bookie" should {
  }
}