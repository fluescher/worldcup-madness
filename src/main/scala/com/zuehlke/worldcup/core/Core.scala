package com.zuehlke.worldcup.core

import akka.actor.ActorSystem
import akka.persistence.Persistent
import akka.actor.ActorRef

trait Core {
  implicit def system: ActorSystem
  
  val bookie: ActorRef
  val gameManager: ActorRef
  val userManager: ActorRef
}

trait BootedCore extends Core {
  implicit val system = ActorSystem("worldcup-madness")

  override val gameManager = system.actorOf(GameManager.props, name ="gameManager")
  override val bookie = system.actorOf(Bookie.props(gameManager), name = "betAccountant")
  override val userManager = system.actorOf(UserManager.props, name ="userManager")

  val matchUpdater = system.actorOf(MatchUpdater.props(gameManager), name ="matchUpdater")
  
  scala.sys.addShutdownHook(shutDownSystem())

  private def shutDownSystem() {
    println("Shutting down...")
    system.shutdown()
  }
}