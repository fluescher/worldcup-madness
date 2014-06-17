package com.zuehlke.worldcup.core

import akka.persistence.Processor
import akka.actor.Props
import akka.persistence.Persistent
import com.zuehlke.worldcup.core.model.User
import akka.actor.ActorLogging

class UserManager extends Processor with ActorLogging {
 import UserManager._
 
 var users: Map[String, User] = Map()
 
 def receive = {
   case Persistent(RegisterUser(user), sequenceNr) =>
     users.contains(user.name) match {
       case true => sender ! UserAlreadyExists
       log.warning(s"user ${user.name} already registered")
       deleteMessage(sequenceNr)
       
       case false => 
       	users += (user.name -> user)
       	log.info(s"user ${user.name} registered" )
       	sender ! UserRegistered
     }
 } 
  
}

object UserManager {
  sealed trait UserManagerMessage
  case class RegisterUser(user: User) extends UserManagerMessage
  case object UserAlreadyExists
  case object UserRegistered
  
  def props(): Props =
    Props(new UserManager())
}

//  def receive = {
//	case Persistent(payload, sequenceNr) => 
//	  println("got journaled: " + payload)
//	  sender ! payload
//	case PersistenceFailure(payload, sequenceNr, cause) =>
//	  println("unable to journal: " + payload)
//	case payload => 
//	  println("unjournaled: " + payload)
//  }
//}

