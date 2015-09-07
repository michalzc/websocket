package chat.actors


case class Subscribe(name: String)
case class Unsubscribe(name: String)
case class Message(sender: String, message: String)
case class LoggedIn(name: String)
case class CanLogIn(name: String)