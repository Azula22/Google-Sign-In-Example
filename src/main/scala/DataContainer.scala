import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object DataContainer {

  implicit val system = ActorSystem()
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val internalHost = "localhost"
  val internalPort = 8080

  val validEmails = Seq("anna.kushyk.u@gmail.com")

}
