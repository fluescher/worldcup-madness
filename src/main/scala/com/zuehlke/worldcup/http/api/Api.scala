package com.zuehlke.worldcup.http.api

import com.zuehlke.worldcup.http.RouteProvider
import spray.routing.Directives
import spray.routing.authentication.BasicHttpAuthenticator
import spray.routing.authentication.UserPassAuthenticator
import spray.routing.authentication.UserPass
import scala.concurrent.Future
import spray.routing.authentication.BasicAuth
import akka.actor.ActorSystem

class Api()(implicit system: ActorSystem) extends RouteProvider with Directives {

  implicit val executionContext = system.dispatcher

  override val route =
    pathPrefix("api") {
      authenticate(BasicAuth(staticUserName _, realm = "worldcup-madness")) { username =>
        path("test") {
          get {
        	complete(s"You are user: $username")
          }
        }
      }
    }

  def staticUserName(userPass: Option[UserPass]): Future[Option[String]] =
    Future {
      if (userPass.exists(up => up.user == "admin" && up.pass == "1234")) Some("Admin")
      else None
    }

}