package com.zuehlke.worldcup.core

import akka.actor.ActorSystem
import akka.persistence.Persistent
import akka.actor.ActorRef
import com.zuehlke.worldcup.core.model.User
import com.zuehlke.worldcup.core.model.Tipp
import com.typesafe.config.ConfigFactory

trait Core {
  implicit def system: ActorSystem
  
  val bookie: ActorRef
  val gameManager: ActorRef
  val userManager: ActorRef
}

trait BootedCore extends Core {
//  System.setProperty("casbah-journal.mongo-journal-url", sys.env("MONGOHQ_URL")+".journal")
//  println("PROP: " + System.getProperty("casbah-journal.mongo-journal-url"))
  implicit val system = ActorSystem("worldcup-madness")//, config.withFallback(backup))
  
  override val gameManager = system.actorOf(GameManager.props, name ="gameManager")
  override val bookie = system.actorOf(Bookie.props(gameManager), name = "bookie")
  override val userManager = system.actorOf(UserManager.props, name ="userManager")

  val matchUpdater = system.actorOf(MatchUpdater.props(gameManager, bookie), name ="matchUpdater")

  /* Test data */
  import UserManager._
  import Bookie._
  val testUser = User("test", "test", 
				 "test", "von Test",
				 "test@test.test")
  userManager ! Persistent(RegisterUser(testUser))
  bookie ! UpdateBets(Tipp("1-bra-cro", testUser, 3, 1, None), bookie)
  bookie ! UpdateBets(Tipp("2-mex-cmr", testUser, 0, 1, None), bookie)
  bookie ! UpdateBets(Tipp("3-uru-crc", testUser, 3, 1, None), bookie)
  
  scala.sys.addShutdownHook(shutDownSystem())

  private def shutDownSystem() {
    println("Shutting down...")
    system.shutdown()
  }
}