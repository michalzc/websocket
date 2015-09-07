package chat

import akka.actor.{Props, ActorRef}

import scala.concurrent.Future

/**
 * Created by michal on 07.09.15.
 */
trait ChatService {
  def chatClientProps(name: String)(out: ActorRef): Props
  def canLogIn(name: String): Future[Boolean]
}
