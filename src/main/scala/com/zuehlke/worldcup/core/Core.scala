package com.zuehlke.worldcup.core

import akka.actor.ActorSystem

trait Core {
  implicit def system: ActorSystem
}

trait BootedCore extends Core {
  implicit val system = ActorSystem("worldcup-madness")

  scala.sys.addShutdownHook(shutDownSystem())

  private def shutDownSystem() {
    println("Shutting down...")
    system.shutdown()
  }
}