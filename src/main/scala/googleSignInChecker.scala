import java.util

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory

import scala.util.Try

/**
  * Created by azula on 17.12.16.
  */

object googleSignInChecker extends (String ⇒ Either[String, Option[String]]) {

  val jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance
  val clientId   : String      = "988935906987-97ups7r848cfk71he7jp360svui2jich.apps.googleusercontent.com"

  val verifier = for {
    httpTransport ← Try(GoogleNetHttpTransport.newTrustedTransport())
    verifier ← Try(new GoogleIdTokenVerifier
    .Builder(httpTransport, jsonFactory)
      .setAudience(util.Arrays.asList(clientId))
      .setIssuer("accounts.google.com")
      .build())
  } yield verifier

  override def apply(tokenFromClient: String): Either[String, Option[String]] =
    verifier
    .map(v ⇒
      Option(v.verify(tokenFromClient)) match {
        case Some(token) ⇒ Right(Option(token.getPayload.getEmail))
        case None ⇒ Left("Token is invalid")
      })
    .recover { case ex ⇒ Left(s"An error occurred ${ex.getMessage}") }
    .get

}
