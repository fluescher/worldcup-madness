package com.zuehlke.worldcup.http.api

import scala.concurrent.Future
import scala.concurrent.duration._
import com.zuehlke.worldcup.http.RouteProvider
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import spray.routing.Directives
import spray.routing.authentication.BasicHttpAuthenticator
import spray.routing.authentication.UserPass
import spray.routing.authentication.BasicAuth
import com.zuehlke.worldcup.core.GameManager
import com.zuehlke.worldcup.core.model.User
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.Group
import com.zuehlke.worldcup.core.model.Team
import com.zuehlke.worldcup.core.model.GameResult
import com.zuehlke.worldcup.core.GameManager.GetGamesResult
import com.zuehlke.worldcup.core.GameManager.GetGroupsResult
import spray.http.HttpHeaders._
import spray.http.AllOrigins
import spray.http.HttpHeader
import spray.routing.authentication.UserPassAuthenticator
import spray.http.HttpChallenge
import spray.http.HttpRequest
import scala.concurrent.ExecutionContext
import spray.http.BasicHttpCredentials
import spray.http.HttpCredentials
import spray.routing.RequestContext
import spray.routing.authentication.HttpAuthenticator

class Api(val gameManager: ActorRef)(implicit system: ActorSystem) extends RouteProvider with Directives {

  import spray.json._
  import DefaultJsonProtocol._
  import spray.httpx.SprayJsonSupport._

  implicit val executionContext = system.dispatcher

  implicit val defaultTimeout = Timeout(5.seconds)

  import WorldcupJsonFormat._

  override val route =
    pathPrefix("api") {
      respondWithHeader(`Access-Control-Allow-Origin`(AllOrigins)) {
        respondWithHeader(`Access-Control-Allow-Credentials`(true)) {
          respondWithHeader(RawHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,PUT")) {
            respondWithHeader(RawHeader("Access-Control-Allow-Headers", "Authorization,Accept,content-type")) {
		        path("register") {
		          post {
		            complete(s"You registered")
		          } 
		        } ~
		          authenticate(new CORSBasicAuth(staticUserName _, realm = "worldcup-madness")) { username =>
		            path("groups") {
		              get {
		                complete {
		                  import GameManager._
		                  (gameManager ? GetGroups).mapTo[GetGroupsResult]
		                }
		              }
		            } ~
		              path("tipps") {
		                get {
		                  complete("tipps")
		                }
		              } ~
		              path("ranking") {
		                get {
		                  complete("ranking")
		                } 
		              } ~
		              path("games") {
		                get {
		                  complete {
		                    import GameManager._
		                    (gameManager ? GetGames).mapTo[GetGamesResult]
		                  }
		                }
		              } ~
		              path("user") {
		                get {
		                  complete {
		                    new User(username, "")
		                  }
		                }
		              }
		          } ~
			      options {
			      	complete { "" }
			      }
		        }
          	}
          }
      }
    }

  def staticUserName(userPass: Option[UserPass]): Future[Option[String]] =
    Future {
      userPass.flatMap(up => {
        if(up.user== "dummy" && up.pass == "1234") {
          Some("dummy")
        } else {
          None
        }
      })
    }

}