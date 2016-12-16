import akka.actor.ActorSystem
import akka.event.slf4j.Logger
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object StartController extends App {

  implicit val system = ActorSystem()
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val host = "localhost"
  val port = 8080

  private val logger = Logger("StartController")

  val allRoutes: Route = start

  val start: Route =  pathEndOrSingleSlash{
    get{
      complete((StatusCodes.OK, "Ok"))
    }
  }

  Http().bindAndHandle(start, host, port).map {_ ⇒
    logger.info(s"Server has started on address $host:$port")
  } recover {
    case ex: Exception => logger.error(s"Server binding failed due to ${ex.getMessage}")
  }

}