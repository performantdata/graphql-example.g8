name := "$name;format="normalize"$"
organization := "$organization$"
scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.5",

  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
)

// Sangria example configuration
import SangriaExample._
webApi   := AkkaHttp
database := H2
