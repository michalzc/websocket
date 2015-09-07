package chat

import javax.inject.{Inject, Singleton}

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import chat.actors.{CanLogIn, ChatClient, ChatRouter, LoggedIn}
import play.api.Logger

import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * Created by michal on 07.09.15.
 */
@Singleton
class ChatServiceAkka @Inject()(system: ActorSystem) extends ChatService {

  val logger = Logger(this.getClass)

  val mainChatActor = system.actorOf(ChatRouter.props, "mainChatActor")

  def chatClientProps(name: String)(out: ActorRef) = ChatClient.props(out, mainChatActor, name)

  def canLogIn(name: String): Future[Boolean] = ask(mainChatActor, CanLogIn(name))(Timeout(5.seconds)).mapTo[Boolean]
}
