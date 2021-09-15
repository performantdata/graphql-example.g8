ThisBuild / organization := "$organization$"
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0"

lazy val root = (project in file("."))
  .dependsOn(database)
  .settings(
    name := "$name;format="normalize"$",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.5",

      "org.scalatest" %% "scalatest" % "3.2.9" % Test,
    ),
  )

lazy val database = project in file("modules/database")

// Sangria example configuration
SangriaExample.webApi := SangriaExample.AkkaHttp
ThisBuild / SangriaExample.database := SangriaExample.H2
