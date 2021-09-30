ThisBuild / organization := "$organization$"
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0"
ThisBuild / scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-target:11",
  "-deprecation",
  "-feature",
  "-unchecked",
)

lazy val root = (project in file("."))
  .dependsOn(codegen)
  .settings(
    name := "$name;format="normalize"$",
    libraryDependencies ++= Seq(
      "com.github.pathikrit" %% "better-files" % "3.9.1",
      "org.scalatest" %% "scalatest" % "3.2.9" % Test,
    ),
    Test / javaOptions += "-Dconfig.resource=application-test.conf",
    Test / fork := true,
  )

/** Sangria example's code generation project. */
lazy val codegen = project

// Sangria example configuration
SangriaExample.webApi := SangriaExample.AkkaHttp
ThisBuild / SangriaExample.database := SangriaExample.H2

// Docker image

import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}
enablePlugins(JavaAppPackaging, DockerPlugin)
dockerBaseImage    := "eclipse-temurin:16-focal"
dockerUpdateLatest := true
dockerExposedPorts := Seq(8080)

// Create writable log and database directories.
Docker / daemonUserUid := None
Docker / daemonUser    := "daemon"
dockerCommands ++= Seq(
  Cmd("USER", "root"),
  ExecCmd("RUN", "mkdir", "database", "logs"),
  ExecCmd("RUN", "chown", "daemon.daemon", "database", "logs"),

  // Update the OS.
//  ExecCmd("RUN", "apt-get", "-qq", "update"),
//  ExecCmd("RUN", "apt-get", "-qq", "upgrade"),

  // Install some useful tools.
//  ExecCmd("RUN", "apt-get", "-qq", "install", "net-tools"),

  Cmd("USER", "daemon"),
)
