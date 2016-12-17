name := "Google Sign In Example"

version := "1.0"

scalaVersion := "2.12.1"

val akkaVersion = "2.4.14"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.0.0",
  "com.softwaremill.akka-http-session" %% "core" % "0.3.0",
  "ch.qos.logback" % "logback-classic" % "1.1.6",
  "com.google.apis" % "google-api-services-oauth2" % "v2-rev124-1.22.0"
)

mainClass in run := Some("StartController")
mainClass in Compile := Some("StartController")