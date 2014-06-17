package com.zuehlke.worldcup.http.api

import spray.routing.authentication.HttpAuthenticator
import spray.http.HttpCredentials
import spray.routing.RequestContext
import spray.routing.authentication.UserPassAuthenticator
import spray.http.HttpRequest
import spray.http.HttpHeaders._
import spray.http.BasicHttpCredentials
import spray.routing.authentication.UserPass
import spray.http.AllOrigins
import spray.http.HttpChallenge
import scala.concurrent.ExecutionContext

class CORSBasicAuth[U](val userPassAuthenticator: UserPassAuthenticator[U], val realm: String)(override implicit val executionContext: ExecutionContext)
  					 extends HttpAuthenticator[U]{
    def authenticate(credentials: Option[HttpCredentials], ctx: RequestContext) = {
	  userPassAuthenticator {
	    credentials.flatMap {
	      case BasicHttpCredentials(user, pass) ⇒ Some(UserPass(user, pass))
	      case _ ⇒ None
	    }
	  }
	}
    
    override def getChallengeHeaders(httpRequest: HttpRequest) = List(
      `Access-Control-Allow-Origin`(AllOrigins),
      `Access-Control-Allow-Credentials`(true),
      RawHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,PUT"),
      RawHeader("Access-Control-Allow-Headers", "Authorization,Accept,content-type"),
      `WWW-Authenticate`(HttpChallenge(scheme = "Basic", realm = realm, params = Map.empty)))
  }