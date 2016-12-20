import DataContainer.Folders._
import akka.http.scaladsl.server.Directives._

object GlobalHelper {

  def getJavascriptFile(name: String) = getFromDirectory(s"$javascriptFolder/$name.js")

  def getHTMLFile(name: String) = getFromDirectory(s"$htmlFolder/$name.html")

  def getCSSFile(name: String) = getFromDirectory(s"$cssFolder/$name.css")

}
