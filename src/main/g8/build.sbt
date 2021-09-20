ThisBuild / organization := "$organization$"
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0"

lazy val root = (project in file("."))
  .settings(
    name := "$name;format="normalize"$",
    libraryDependencies ++= Seq(
      "com.github.pathikrit" %% "better-files" % "3.9.1",

      "org.scalatest" %% "scalatest" % "3.2.9" % Test,
    ),
  )

// Sangria example configuration
SangriaExample.webApi := SangriaExample.AkkaHttp
ThisBuild / SangriaExample.database := SangriaExample.H2

// Docker image
enablePlugins(JavaAppPackaging)
dockerBaseImage    := "eclipse-temurin:16-focal"
dockerUpdateLatest := true
dockerExposedPorts := Seq(8080)
