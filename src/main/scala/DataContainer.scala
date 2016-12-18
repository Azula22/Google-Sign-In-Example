import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.softwaremill.session.{SessionConfig, SessionManager}
import com.typesafe.config.ConfigFactory

object DataContainer {

  implicit val system = ActorSystem()
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val internalHost = "localhost"
  val internalPort = 8080

  val validEmails = Seq("anna.kushyk.u@gmail.com")

  val config = ConfigFactory.load()

  implicit val sessionManager = new SessionManager[String](SessionConfig.default("pEJAPFMd7J32lXyHuxriMUaxVdlZrFqBILIG2ZHms5KbhUNlBbNnF9tX3k0q7jKhx2CGNDZdUwZfPXVlHUEXd1pLi4r3WAVgSIE9knW63im67zzDISVIZXI86hncptf9yiPoaKaoj3Gv"))

}
