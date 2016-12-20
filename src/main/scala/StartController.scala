import DataContainer.Folders._
import DataContainer.Routes._
import DataContainer._
import akka.event.slf4j.Logger
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Route}
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._

object StartController extends App {

  private val logger = Logger("StartController")

  def start: Route = path(login) {
    get {
      getFromDirectory(s"$htmlFolder/login.html")
    } ~ (post & formField('token.as[String])) {
      token ⇒
        val check = googleSignInChecker(token)
        check match {
          case Right(Some(mailAddress)) if validEmails.contains(mailAddress) ⇒
            logger.info(s"Sign in user $mailAddress")
            setSession[String](oneOff[String], usingCookies, token)(complete(StatusCodes.OK → mailAddress))
          case Left(errMessage)                                              ⇒
            logger.error(s"An error occurred $errMessage")
            complete(StatusCodes.InternalServerError → s"An error occurred $errMessage")
          case other                                                         ⇒
            reject(AuthorizationFailedRejection)
        }
    }
  }

  def generalAccessResources = get {
    path("login.js") {
      getFromDirectory(s"$javascriptFolder/login.js")
    } ~ path("global.js") {
      getFromDirectory(s"$javascriptFolder/global.js")
    }
  }

  def mainPage = pathEndOrSingleSlash {
    requiredSession[String](oneOff[String], usingCookies) { _ ⇒
        complete(StatusCodes.OK → "Ok")
    }
  }

  val allRoutes =
    start ~
    mainPage ~
    generalAccessResources

  Http().bindAndHandle(allRoutes, internalHost, internalPort).map { serverBinding ⇒
      logger.info(s"Server has started on address ${serverBinding.localAddress.getAddress.toString}:${serverBinding.localAddress.getPort.toString}")
  } recover {
    case ex: Exception => logger.error(s"Server binding failed due to ${ex.getMessage}")
  }

}