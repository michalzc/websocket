package chat

import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}

/**
 * Created by michal on 07.09.15.
 */
class ChatModule extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
    bind[ChatService].to[ChatServiceAkka].eagerly()
  )
}
