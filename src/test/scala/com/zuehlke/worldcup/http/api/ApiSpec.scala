package com.zuehlke.worldcup.http.api

import org.scalatest.Matchers
import spray.testkit.Specs2RouteTest
import org.scalatest.WordSpecLike
import spray.testkit.ScalatestRouteTest
import org.scalatest.WordSpec
import spray.http.StatusCodes
import spray.routing.HttpService
import spray.http._
import ContentTypes._
import spray.http.HttpHeaders.Authorization
import spray.http.BasicHttpCredentials
import akka.actor.Actor
import akka.actor.Props
import com.zuehlke.worldcup.core.UserManager.UserRegistered
import com.zuehlke.worldcup.core.UserManager.UserAlreadyExists

class ApiSpec extends WordSpec with ScalatestRouteTest with Matchers {

  def actorRefFactory = system
  
  val registerUserActor = system.actorOf(DummyActor.props(UserRegistered))
  val userAlreadyExistsActor = system.actorOf(DummyActor.props(UserAlreadyExists))
  
  val underTest = new Api(system.actorOf(DummyActor.props("test")), 
		  				  registerUserActor,
		  				  system.actorOf(DummyActor.props("test")))
  
  "The api endpoint" should {
    "require basic auth" in {
      Get("/api/user") ~> HttpService.sealRoute(underTest.route) ~> check {
        status should be(StatusCodes.Unauthorized)
      }
    }
    
    "not allow a registration of a existing user" in {
      val underTest = new Api(system.actorOf(DummyActor.props("test")), 
		  				  userAlreadyExistsActor,
		  				  system.actorOf(DummyActor.props("test")))
      
      Post("/api/register", HttpEntity(`application/json`, jsonMarshalledUser) ) ~> 
        addHeader(Authorization(BasicHttpCredentials("Bob", "TheBuilder"))) ~> 
        underTest.route ~> check {
          status should be(StatusCodes.BadRequest)
      }
    }
    
    "allow registration of a new user" in {
      val underTest = new Api(system.actorOf(DummyActor.props("test")), 
		  				  registerUserActor ,
		  				  system.actorOf(DummyActor.props("test")))
      
      Post("/api/register", HttpEntity(`application/json`, jsonMarshalledUser) ) ~> 
        addHeader(Authorization(BasicHttpCredentials("Bob", "TheBuilder"))) ~> 
        underTest.route ~> check {
          status should be(StatusCodes.OK)
      }
    }
  }
  
  val jsonMarshalledUser = """{"name":"fllu", "password":"blobb",
		  					   "forename": "fl", "surname": "lu",
		  					   "email": "String"}"""
  
  class DummyActor(val response: Any) extends Actor {
    def receive = {
      case _ => sender ! response
    }
  }
  object DummyActor {
    def props(response: Any) = Props(new DummyActor(response))
  }
  
}