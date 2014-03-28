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

trait HttpServer extends RouteConcatenation {
  this: Core =>

  val routes = new StaticContent().route

  IO(Http)(system) ! Http.Bind(system.actorOf(Props(new RoutedHttpServer(routes))), "0.0.0.0", port = 8080)
}

trait RouteProvider {
  val route: Route
}

class RoutedHttpServer(private val route: Route) extends Actor with HttpService with SprayActorLogging {
  implicit def actorRefFactory = context

  def receive = runRoute(route)
}