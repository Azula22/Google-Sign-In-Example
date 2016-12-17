import java.net.URL

import DataContainer._
import akka.event.slf4j.Logger
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.HttpChallenges
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.AuthenticationFailedRejection.CredentialsRejected
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{AuthenticationFailedRejection, Route}

import scala.util.{Failure, Success}

object StartController extends App {

  private val logger = Logger("StartController")

  def start: Route = path("login") {
    get {
      getFromResource("signIn.html", ContentTypes.`text/html(UTF-8)`)
    } ~ (post & formFields('email.as[String], 'token.as[String])) { (email, token) ⇒
      googleSignInChecker(token) match {
        case Right(true) if validEmails.contains(email) ⇒
          logger.info(s"Sign in user $email")
          complete(StatusCodes.OK → email)
        case Left(errMessage) ⇒
          logger.error(s"An error occured $errMessage")
          complete(StatusCodes.InternalServerError → s"An error occured $errMessage")
        case other ⇒ reject(AuthenticationFailedRejection(CredentialsRejected, HttpChallenges.oAuth2("None")))
      }
    }
  }

  def mainPage = pathEndOrSingleSlash(complete(StatusCodes.OK → "Ok"))

  val allRoutes = start ~ mainPage

  Http().bindAndHandle(allRoutes, internalHost, internalPort).map { serverBinding ⇒
      logger.info(s"Server has started on address ${serverBinding.localAddress.getAddress.toString}:${serverBinding.localAddress.getPort.toString}")
  } recover {
    case ex: Exception => logger.error(s"Server binding failed due to ${ex.getMessage}")
  }

}