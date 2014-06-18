package com.zuehlke.worldcup.core

import org.scalatest.Matchers
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.actor.ActorSystem
import org.scalatest.WordSpecLike
import scala.concurrent.duration._
import akka.testkit.ImplicitSender
import akka.persistence.Persistent

class BookieSpec extends TestKit(ActorSystem("worldcup-madness")) with WordSpecLike with Matchers with ImplicitSender {
  "A bookie" should {
    "reply with the payload when a persisted message was sent" in {
      val bookie = system.actorOf(Bookie.props)
      
    }
  }
}