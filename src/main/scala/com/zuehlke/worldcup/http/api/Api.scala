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
import com.zuehlke.worldcup.core.model.Team
import com.zuehlke.worldcup.core.model.GameResult
import com.zuehlke.worldcup.core.GameManager.GetGamesResult
import spray.http.HttpHeaders._
import spray.http.AllOrigins
import spray.http.HttpHeader
import spray.routing.authentication.UserPassAuthenticator
import spray.http.HttpChallenge
import spray.http.HttpRequest
import scala.concurrent.ExecutionContext
import spray.http.BasicHttpCredentials
import spray.http.StatusCodes
import com.zuehlke.worldcup.core.UserManager
import akka.persistence.Persistent
import spray.http.HttpCredentials
import spray.routing.RequestContext
import spray.routing.authentication.HttpAuthenticator
import com.zuehlke.worldcup.core.RankingCalculator
import com.zuehlke.worldcup.core.RankingCalculator
import com.zuehlke.worldcup.core.RankingCalculator
import com.zuehlke.worldcup.core.model.Ranking
import com.zuehlke.worldcup.core.model.Tipp
import com.zuehlke.worldcup.core.Bookie
import spray.routing.PathMatcher

class Api(val gameManager: ActorRef, val userManager: ActorRef, val bookie: ActorRef)(implicit system: ActorSystem) extends RouteProvider with Directives {

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
          respondWithHeader(RawHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT")) {
            respondWithHeader(RawHeader("Access-Control-Allow-Headers", "Authorization, Accept, Content-Type, x-requested-with")) {
		        path("register") {
		          post {
		            entity(as[User]) { user => 
			            complete {
			              import UserManager._
			              (userManager ? Persistent(RegisterUser(user))).map({
			                case UserAlreadyExists 	=> StatusCodes.BadRequest
			                case UserRegistered 	=> StatusCodes.OK 
			              })
			              }
			            }
		            }
		        } ~
		          authenticate(new CORSBasicAuth(staticUserName _, realm = "worldcup-madness")) { user =>
		              path("tipps" ~ (Slash ~ regex2PathMatcher("\\w+".r)).?) { pathUser =>
		                get {
		                  complete {
		                    import Bookie._
		                    pathUser match {
		                      case None 		=> (bookie ? GetAllBets).mapTo[GetAllBetsResult].map(_.bets)
		                      case Some(name)	=> (bookie ? GetBets(name)).mapTo[GetBetsResult].map(_.bets)
		                    }
		                  }
		                } ~
		                  post {
		                    entity(as[Tipp]) { tipp => 
		                      complete {
		                        import Bookie._
		                        (bookie ? Persistent(PlaceBet(tipp.updateUser(user)))).map({
		                          case BetPlaced  => StatusCodes.OK 
		                          case BetInvalid => StatusCodes.BadRequest 
		                        })
		                      }
		                    }
		                  }
		              } ~
		              path("ranking") {
		                get {
		                  complete {
		                    import Bookie._
		                     import GameManager._
		                    (gameManager ? GetGames).mapTo[GetGamesResult]
		                    						.map(_.games)
		                    						.map(games =>
		                        (bookie ? CalculatePoints(games)).mapTo[RankingResult].map(_.rankings)
		                    )
		                  }
		                } 
		              } ~
		              path("games") {
		                get {
		                  complete {
		                    import GameManager._
		                    (gameManager ? GetGames).mapTo[GetGamesResult].map(_.games)
		                  }
		                }
		              } ~
		              path("user") {
		                get {
		                  complete {
		                    user
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

  def staticUserName(userPass: Option[UserPass]): Future[Option[User]] = {
    userPass match {
      case None 			=> Future(None)
      case Some(userPass) 	=>
		import UserManager._
		(userManager ? AuthorizeUser(userPass.user, userPass.pass)).map({
		  case UserAuthorized(user) => Some(user)
		  case _					=> None
		})
    }
  }	
}