package chat.controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.util.Timeout
import chat.services.ChatService
import play.api.Play.current
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * Created by mzajac on 08.09.15.
 */
class ChatController @Inject()(chatService: ChatService, system: ActorSystem) extends Controller {

    def index = Action {
        Ok(chat.views.html.index())
    }

    def socket = WebSocket.tryAcceptWithActor[String, String] { request =>

        import system.dispatcher
        implicit val timeout = Timeout(5.seconds)

        val nameOption = request.queryString.get("name").flatMap(_.headOption)

        nameOption
            .map { name => chatService.canLogIn(name).map((name, _)) }
            .map(fut => fut.map { tup =>
                val (name, canLogin) = tup
                if (canLogin) Right(chatService.chatClientProps(name) _)
                else Left(Forbidden("Name already used!"))
            }).getOrElse(Future.successful(Left(Forbidden("Invalid login!"))))
    }
}
