package com.zuehlke.worldcup.http.content

import akka.actor.Actor
import akka.actor.Props
import spray.routing.Directive.pimpApply
import spray.routing.Directives
import spray.routing.HttpService
import spray.util.SprayActorLogging
import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext
import com.zuehlke.worldcup.http.RouteProvider

class StaticContent()(implicit system: ActorSystem) extends RouteProvider with Directives {
  override val route =
    path("ui") {
      get {
        getFromResource("ui/index.html")
      }
    } ~
    pathPrefix("ui") {
      getFromResourceDirectory("ui")
    }
}

