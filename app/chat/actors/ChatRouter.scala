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
    }

    case Unsubscribe(name) => {
      clients.get(name).filter(c => c == sender()).foreach{ ref => clients -= name }
    }

    case message @ Message(sender, _) => {
      clients.filter(_._1 != sender).foreach(_._2 ! message)
    }
  }

  override def preStart() {
    log.info("Chat started")
  }
}
