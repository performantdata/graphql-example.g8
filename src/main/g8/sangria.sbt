/* Build script related to the Sangria example code.
 *
 * It should not be necessary for users of this template project to modify this code.
 */
ThisBuild / libraryDependencies ++= {
  val jacksonVersion = "2.12.5"
  val log4jVersion   = "2.14.1"
  val reactVersion   = "17.0.2"
  val slickVersion   = "3.3.3"

  Seq(
    "com.github.pureconfig" %% "pureconfig" % "0.16.0",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
    "org.liquibase" % "liquibase-core" % "4.4.3",
    "org.sangria-graphql" %% "sangria" % "2.1.3",

    // logging
    "org.apache.commons" % "commons-compress" % "1.21",
    "org.tukaani" % "xz" % "1.9",
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion,
    "org.apache.logging.log4j" % "log4j-api"        % log4jVersion,
    "org.apache.logging.log4j" % "log4j-core"       % log4jVersion,
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % jacksonVersion,
    "com.fasterxml.jackson.core"       % "jackson-databind"        % jacksonVersion,

    // GraphiQL
    "org.webjars" % "webjars-locator-core" % "0.47",
    // Newer versions of GraphiQL depend on org.webjars.npm:n1ru4l__push-pull-async-iterable-iterator:[2.1.4,3), which aren't webjars yet.
    "org.webjars.npm" % "graphiql" % "1.3.2",
    "org.webjars.npm" % "react"     % reactVersion,
    "org.webjars.npm" % "react-dom" % reactVersion,

    // Slick
    "com.typesafe.slick" %% "slick"          % slickVersion,
    "com.typesafe.slick" %% "slick-codegen"  % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,

    // for code generation
    "info.picocli" % "picocli" % "4.6.1" % Provided,  //TODO only present to code gen scope
  )
}

// database

ThisBuild / libraryDependencies ++= {
  import SangriaExample._

  database.value match {
    case H2 =>
      Seq(
        "com.h2database" % "h2" % "1.4.200",
      )
  }
}

// Web API

ThisBuild / libraryDependencies ++= {
  import SangriaExample._

  webApi.value match {
    case AkkaHttp =>
      val akkaHttpVersion        = "$akka_http_version$"
      val akkaVersion            = "$akka_version$"
      val sangriaAkkaHttpVersion = "0.0.2"

      Seq(
        "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
        "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
        "com.typesafe.akka" %% "akka-actor-typed"     % akkaVersion,
        "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

        "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
        "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,

        "org.sangria-graphql" %% "sangria-akka-http-core"  % sangriaAkkaHttpVersion,
        "org.sangria-graphql" %% "sangria-akka-http-circe" % sangriaAkkaHttpVersion,
      )
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
  case class Database(db: DatabaseParameters)
  case class DatabaseParameters(url: String, user: Option[String], password: Option[String])

  val logger      = streams.value.log
  val baseDir     = (ThisBuild / baseDirectory).value.toPath
  val resourceDir = (Compile / resourceDirectory).value

  // Read the database URI from the configuration.
  val config =
    ConfigSource.file(resourceDir / "application.conf").at("sangria").loadOrThrow[SangriaConfig].database.db
  val url      = config.url
  val user     = config.user.getOrElse("")
  val password = config.password.getOrElse("")

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
  val className = "SangriaExampleCodeGenerator"

  runner.value.run("slick.codegen.SourceCodeGenerator",
    classpath, Seq(profile, driver, url, outputDir.getPath, pkg, user, password, "true", className, "true"), logger
  ).recover {
    case t: Throwable => sys.error(t.getMessage)
  }

  ((outputDir / pkg.replace('.', '/')) ** "*.scala").get()
}
