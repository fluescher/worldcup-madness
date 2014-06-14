package com.zuehlke.worldcup.core

import akka.actor.ActorSystem
import akka.persistence.Persistent

trait Core {
  implicit def system: ActorSystem
}

trait BootedCore extends Core {
  implicit val system = ActorSystem("worldcup-madness")

  val betAccountant = system.actorOf(Bookie.props, name = "betAccountant")
  
  betAccountant ! Persistent("foo") // will be journaled
  betAccountant ! "bar"
  
  scala.sys.addShutdownHook(shutDownSystem())

  private def shutDownSystem() {
    println("Shutting down...")
    system.shutdown()
  }
}