package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.util.Timeout
import chat.ChatService
import play.api.mvc._
import play.api.Play.current

import scala.concurrent.Future
import scala.concurrent.duration._

class Application @Inject()(chatService: ChatService,  system: ActorSystem) extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def socket = WebSocket.tryAcceptWithActor[String, String] { request =>

    import system.dispatcher
    implicit val timeout = Timeout(5.seconds)

    val nameOption = request.queryString.get("name").flatMap(_.headOption)

    val response = nameOption
      .map{ name => chatService.canLogIn(name).map((name, _)) }
      .map( fut => fut.map{ tup =>
        val (name, canLogin) = tup
        if(canLogin) Right(chatService.chatClientProps(name) _)
        else Left(Forbidden("Name already used!"))
      }).getOrElse(Future.successful(Left(Forbidden("Invalid login!"))))

    response
  }
}
