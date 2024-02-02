import com.typesafe.sbt.packager.docker.DockerChmodType

import java.time.Instant

ThisBuild / organization := "$organization$"
ThisBuild / versionScheme := Some("semver-spec")

ThisBuild / scalaVersion := "2.13.6"
ThisBuild / scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-target:17",
  "-deprecation",
  "-feature",
  "-unchecked",
)

/** Static Web files subproject. */
lazy val `static` = (project in file("static"))
  .settings(
    name := "$name;format="normalize"$-static",
    description := "Static Web files for the \"$name$\" system.",
  )

lazy val root = (project in file("."))
  .dependsOn(codegen)
  .enablePlugins(GitVersioning, JavaAppPackaging, DockerPlugin)
  .settings(
    name := "$name;format="normalize"$",
    libraryDependencies ++= Seq(
      "com.github.pathikrit" %% "better-files" % "3.9.1",
      "org.scalatest" %% "scalatest" % "3.2.9" % Test,
    ),
    Test / javaOptions += "-Dconfig.resource=application-test.conf",
    Test / fork := true,

    // sbt-native-packager settings

    makeBatScripts := Seq.empty,
    dockerBaseImage := "debian:12.4-slim",
    dockerLabels ++= Map(
      "org.opencontainers.image.created"       -> Instant.now.toString,
      "org.opencontainers.image.version"       -> version.value,
      "org.opencontainers.image.revision"      -> git.gitHeadCommit.value.getOrElse(""),
      "org.opencontainers.image.vendor"        -> organizationName.value,
      "org.opencontainers.image.description"   -> description.value.replace(""""""", """\\""""),
      "org.opencontainers.image.base.name"     -> dockerBaseImage.value,
    ),
    dockerEnvVars ++= Map("LANG" -> "C.UTF-8"),
    dockerExposedPorts := Seq(8080),
    dockerUpdateLatest := true,
    // We need a writable, searchable directory for log files.
    dockerAdditionalPermissions +=
      (DockerChmodType.UserGroupWriteExecute, (Docker / defaultLinuxInstallLocation).value + "/logs"),
  )

/** Sangria example's code generation project. */
lazy val codegen = project

// Sangria example configuration
ThisBuild / SangriaExample.database := SangriaExample.H2
