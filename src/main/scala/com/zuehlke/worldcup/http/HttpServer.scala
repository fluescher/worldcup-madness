package com.zuehlke.worldcup.http

import spray.routing.RouteConcatenation
import spray.routing.Route
import spray.routing.HttpService
import spray.util.SprayActorLogging
import akka.actor.Actor
import akka.io.IO
import spray.can.Http
import com.zuehlke.worldcup.core.Core
import akka.actor.Props
import com.zuehlke.worldcup.http.content.StaticContent
import com.zuehlke.worldcup.config.RuntimeConfiguration
import akka.actor.ActorLogging
import com.zuehlke.worldcup.http.api.Api

trait HttpServer extends RouteConcatenation {
  this: Core with RuntimeConfiguration =>

  val routes = new StaticContent().route ~ new Api(gameManager, userManager, bookie).route

  IO(Http)(system) ! Http.Bind(system.actorOf(Props(new RoutedHttpServer(routes))), ip, port = port)
}

trait RouteProvider {
  val route: Route
}

class RoutedHttpServer(private val route: Route) extends Actor with HttpService with ActorLogging {
  implicit def actorRefFactory = context

  def receive = runRoute(route)
}