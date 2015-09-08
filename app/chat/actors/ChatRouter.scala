package chat.actors

import akka.actor.{ActorRef, Actor, ActorLogging, Props}

/**
 * Created by michal on 07.09.15.
 */
object ChatRouter  {

  def props = Props[ChatRouter]

}

class ChatRouter extends Actor with ActorLogging {

  var clients: Map[String, ActorRef] = Map()


  def receive = {
    case CanLogIn(name) => {
      sender() ! (!clients.contains(name))
    }

    case LoggedIn(name) => {
      clients += (name -> sender())
      clients.values.foreach(_ ! Message("SYSTEM", s"User $name joined"));
    }

    case Unsubscribe(name) => {
      clients.get(name).filter(c => c == sender()).foreach{ ref => clients -= name }
      clients.values.foreach(_ ! Message("SYSTEM", s"User $name left"));
    }

    case message @ Message(sender, _) => {
      clients.values.foreach(_ ! message)
    }
  }

  override def preStart() {
    log.info("Chat started")
  }
}
