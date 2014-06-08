package com.zuehlke.worldcup.http.content

import com.zuehlke.worldcup.http.RouteProvider

import akka.actor.ActorSystem
import spray.http.StatusCodes
import spray.routing.Directive.pimpApply
import spray.routing.Directives
import spray.util.SprayActorLogging

class StaticContent()(implicit system: ActorSystem) extends RouteProvider with Directives {
  override val route =
    path("") {
      get {
        getFromResource("ui/index.html")
      }
    } ~
    pathPrefix("") {
      getFromResourceDirectory("ui/")
    }
}

