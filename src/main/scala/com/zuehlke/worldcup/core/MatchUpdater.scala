package com.zuehlke.worldcup.core

import scala.concurrent.Future
import spray.http._
import spray.client.pipelining._
import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import scala.concurrent.duration._
import akka.actor.ActorLogging
import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport._
import com.zuehlke.worldcup.core.model.Team
import com.zuehlke.worldcup.core.model.Game
import com.zuehlke.worldcup.core.model.GameResult
import spray.json._

class MatchUpdater(val gameManager: ActorRef, val bookie: ActorRef) extends Actor with ActorLogging {
  import GameManager._
  import MatchUpdater._
  import context._
  val system = context.system
  
  val tick =
    context.system.scheduler.schedule(0.seconds, 10.minutes, self, UpdateGames)
  
  override def receive = {
    case UpdateGames => 
      log.info("Updating games")
      val future = queryAndMap()
      future.onSuccess({case games => gameManager ! GamesUpdated(games); bookie ! Bookie.UpdateGames(games)})
      future.onFailure({case error => println(error)})
  }
  
  object ResponseProtocol extends DefaultJsonProtocol {
	implicit val teamFormat = jsonFormat3(FootballTeam)
    implicit val eventFormat = jsonFormat2(Event)
	implicit val teamResultFormat = jsonFormat2(TeamResult)
	implicit val roundFormat = jsonFormat4(Round)
	implicit val footballGameFormat =jsonFormat13(FootballGame)
	implicit val roundResult = jsonFormat3(RoundResult)
  }
  import ResponseProtocol._
  
  val pipeline: HttpRequest => Future[TeamResult] = sendReceive ~> unmarshal[TeamResult]
  val roundPipeline: HttpRequest => Future[RoundResult] = sendReceive ~> unmarshal[RoundResult]
  
  def queryAndMap(): Future[List[Game]] = 
    queryTeams().flatMap(teams => {
	  queryGames().map(games => games.map(game => {
	    mapGameToTeams(game, teams)  
	  }))
    })
  
  def mapGameToTeams(game: (Int, FootballGame), teams: List[Team]): Game = 
    Game(game._2.play_at, game._1, findTeam(game._2.team1_key, teams), findTeam(game._2.team2_key, teams), getResult(game._2))
    
  def getResult(game: FootballGame): Option[GameResult] = (game.score1, game.score2) match {
    case (Some(score1), Some(score2)) => Some(GameResult(score1, score2))
    case _ => None
  }
    
  def findTeam(key: String, teams: List[Team]) = teams.find(_.abbreviation == key).getOrElse(null)
    
  def queryGames(): Future[List[(Int, FootballGame)]] = all((1 to 20).map(i => {
    roundPipeline(Get(s"http://footballdb.herokuapp.com/api/v1/event/world.2014/round/$i"))
    			  .map(_.games).map(_.map((i, _)))
  }).toList).map(_.flatten)
    
  def queryTeams(): Future[List[Team]] = 
    pipeline(Get("http://footballdb.herokuapp.com/api/v1/event/world.2014/teams"))
    	.map(_.teams)
    	.map(_.map(toWorldcupTeam(_)))
    	
  def toWorldcupTeam(footballTeam: FootballTeam): Team =
    Team(footballTeam.title, footballTeam.key)

  def all[T](fs: List[Future[T]]): Future[List[T]] = 
     fs.foldRight(Future(Nil: List[T]))((f, fs2) =>
       for {
         x <- f
         xs <- fs2
       } yield (x::xs))
}

object MatchUpdater {
  case object UpdateGames
  
  case class TeamResult(event: Event, teams: List[FootballTeam])
  case class Event(key: String, title: String)
  case class FootballTeam(key: String, title: String, code: String)
  case class Round(pos: Int, title: String, start_at: String, end_at: String)
  case class FootballGame(team1_key: String,
		  				  team1_title: String, team1_code: String,
	  				  	  team2_key: String, team2_title: String,
	  				  	  team2_code: String, play_at: String,
	  				  	  score1: Option[Int], score2: Option[Int],
	  				  	  score1ot: Option[Int], score2ot: Option[Int],
	  				  	  score1p: Option[Int], score2p: Option[Int])
  case class RoundResult(event: Event, round: Round, games: List[FootballGame])
  
  def props(gameManager: ActorRef, bookie: ActorRef) =
    Props(new MatchUpdater(gameManager, bookie))
}