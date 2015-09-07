package chat.actors

import akka.actor.{ActorLogging, Actor, ActorRef, Props}

/**
 * Created by michal on 07.09.15.
 */
object ChatClient {
  def props(out: ActorRef, router: ActorRef, name: String) = Props(classOf[ChatClient], out, router, name)
}

class ChatClient(out: ActorRef, router: ActorRef, name: String) extends Actor with ActorLogging {

  def receive = {
    case msg: String => {
      router ! Message(name, msg)
    }

    case Message(senderName, message) => {
      out ! s"<$senderName> $message"
    }
  }

  override def preStart() {
    router ! LoggedIn(name)
  }
}