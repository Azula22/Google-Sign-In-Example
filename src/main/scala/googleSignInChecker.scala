import java.util

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory

import scala.util.Try

/**
  * Created by azula on 17.12.16.
  */

object googleSignInChecker extends (String ⇒ Either[String, Boolean]) {

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

  override def apply(tokenFromClient: String): Either[String, Boolean] =
    verifier
    .map(v ⇒ Option(v.verify(tokenFromClient)).fold[Either[String, Boolean]](Right(false))(_ ⇒ Right(true)))
    .recover { case ex ⇒ Left(ex.getMessage) }
    .get

}
