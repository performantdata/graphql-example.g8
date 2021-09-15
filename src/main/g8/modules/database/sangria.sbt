/* Build script related to the Sangria example code.
 *
 * It should not be necessary for users of this template project to modify this code.
 */
libraryDependencies ++= {
  Seq(
    "com.github.pureconfig" %% "pureconfig" % "0.16.0",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
    "org.liquibase" % "liquibase-core" % "4.4.3",

    // Slick
    "com.typesafe.slick" %% "slick-codegen" % "3.3.3",

    // for code generation
    "info.picocli" % "picocli" % "4.6.1" % Provided,
    "org.slf4j" % "slf4j-simple" % "1.7.32" % Provided,
  ) ++
  {  // database
    import SangriaExample._

    database.value match {
      case H2 =>
        Seq(
          "com.h2database" % "h2" % "1.4.200",
        )
    }
  }
}

// Slick code generation

Compile / sourceGenerators += domainClassGeneration.taskValue

lazy val domainClassGeneration = taskKey[Seq[File]]("Generate the domain classes from the database schema")
domainClassGeneration := {
  import SangriaExample._
  import pureconfig._
  import pureconfig.generic.auto._

  case class SangriaConfig(database: Database)
  case class Database(url: String, user: Option[String], password: Option[String])

  val logger      = streams.value.log
  val baseDir     = (ThisBuild / baseDirectory).value.toPath
  val resourceDir = (Compile / resourceDirectory).value

  // Read the database URI from the configuration.
  val config = ConfigSource.file(resourceDir / "application.conf").at("sangria").loadOrThrow[SangriaConfig]
  val url = config.database.url

  // Run Liquibase

  val classpath = (Compile / dependencyClasspath).value.files
  val changelog = baseDir.relativize((resourceDir / "liquibase.xml").toPath).toString

  runner.value.run("liquibase.integration.commandline.LiquibaseCommandLine",
    classpath, Seq("update", s"--changelog-file=\$changelog", s"--url=\$url"), logger
  ).recover {
    case t: Throwable => sys.error(t.getMessage)
  }

  // Run Slick

  val profile   = slickProfile(database.value)
  val driver    = jdbcDriver(database.value)
  val outputDir = (Compile / sourceManaged).value / "slick"
  val pkg       = "$package$.domain"

  runner.value.run("slick.codegen.SourceCodeGenerator",
    classpath, Seq(profile, driver, url, outputDir.getPath, pkg), logger
  ).recover {
    case t: Throwable => sys.error(t.getMessage)
  }

  Seq(outputDir / pkg.replace('.', '/') / "Tables.scala")
}
