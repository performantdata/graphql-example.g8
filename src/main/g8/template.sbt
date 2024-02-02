/* Build script related to the GraphQL example code.
 *
 * It should not be necessary for users of this template project to modify this code.
 */
libraryDependencies ++= {
  val jacksonVersion = "2.15.2"
  val log4jVersion   = "2.20.0"
  val reactVersion   = "18.2.0"

  Seq(
    "com.github.pureconfig" %% "pureconfig" % Versions.pureconfig,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
    "org.liquibase" % "liquibase-core" % "4.20.0",
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
    "org.webjars" % "webjars-locator-core" % "0.55",
    "org.webjars.npm" % "graphiql" % "3.1.1",
    "org.webjars.npm" % "graphiql__plugin-explorer" % "1.0.3",
    "org.webjars.npm" % "react"     % reactVersion,
    "org.webjars.npm" % "react-dom" % reactVersion,

    // Slick
    "com.typesafe.slick" %% "slick"          % Versions.slick,
    "com.typesafe.slick" %% "slick-codegen"  % Versions.slick,
    "com.typesafe.slick" %% "slick-hikaricp" % Versions.slick,

    // for code generation
    "info.picocli" % "picocli" % "4.6.1" % Provided,  //TODO only present to code gen scope
  )
}
dependencyOverrides ++= Seq(
  // pinned versions for stability of the template
  "org.webjars.npm" % "argparse" % "2.0.1",
  "org.webjars.npm" % "aria-hidden" % "1.2.3",
  "org.webjars.npm" % "babel__runtime" % "7.21.0",
  "org.webjars.npm" % "clsx" % "1.2.1",
  "org.webjars.npm" % "codemirror" % "5.65.16",
  "org.webjars.npm" % "codemirror-graphql" % "2.0.10",
  "org.webjars.npm" % "copy-to-clipboard" % "3.3.3",
  "org.webjars.npm" % "detect-node-es" % "1.1.0",
  "org.webjars.npm" % "entities" % "2.1.0",
  "org.webjars.npm" % "floating-ui__core" % "1.6.0",
  "org.webjars.npm" % "floating-ui__dom" % "1.6.1",
  "org.webjars.npm" % "floating-ui__react-dom" % "2.0.8",
  "org.webjars.npm" % "floating-ui__utils" % "0.2.1",
  "org.webjars.npm" % "framer-motion" % "6.5.1",
  "org.webjars.npm" % "framesync" % "6.0.1",
  "org.webjars.npm" % "get-nonce" % "1.0.1",
  "org.webjars.npm" % "graphiql-explorer" % "0.9.0",
  "org.webjars.npm" % "graphiql__react" % "0.20.3",
  "org.webjars.npm" % "graphiql__toolkit" % "0.9.1",
  "org.webjars.npm" % "graphql-language-service" % "5.2.0",
  "org.webjars.npm" % "headlessui__react" % "1.7.17",
  "org.webjars.npm" % "hey-listen" % "1.0.8",
  "org.webjars.npm" % "invariant" % "2.2.4",
  "org.webjars.npm" % "isobject" % "3.0.1",
  "org.webjars.npm" % "is-plain-object" % "2.0.4",
  "org.webjars.npm" % "is-primitive" % "3.0.1",
  "org.webjars.npm" % "js-tokens" % "4.0.0",
  "org.webjars.npm" % "linkify-it" % "3.0.3",
  "org.webjars.npm" % "loose-envify" % "1.4.0",
  "org.webjars.npm" % "markdown-it" % "12.3.2",
  "org.webjars.npm" % "mdurl" % "1.0.1",
  "org.webjars.npm" % "meros" % "1.3.0",
  "org.webjars.npm" % "motionone__dom" % "10.12.0",
  "org.webjars.npm" % "n1ru4l__push-pull-async-iterable-iterator" % "3.2.0",
  "org.webjars.npm" % "nullthrows" % "1.1.1",
  "org.webjars.npm" % "popmotion" % "11.0.3",
  "org.webjars.npm" % "radix-ui__primitive" % "1.0.1",
  "org.webjars.npm" % "radix-ui__react-arrow" % "1.0.3",
  "org.webjars.npm" % "radix-ui__react-collection" % "1.0.3",
  "org.webjars.npm" % "radix-ui__react-compose-refs" % "1.0.1",
  "org.webjars.npm" % "radix-ui__react-context" % "1.0.1",
  "org.webjars.npm" % "radix-ui__react-dialog" % "1.0.5",
  "org.webjars.npm" % "radix-ui__react-direction" % "1.0.1",
  "org.webjars.npm" % "radix-ui__react-dismissable-layer" % "1.0.5",
  "org.webjars.npm" % "radix-ui__react-dropdown-menu" % "2.0.6",
  "org.webjars.npm" % "radix-ui__react-focus-guards" % "1.0.1",
  "org.webjars.npm" % "radix-ui__react-focus-scope" % "1.0.4",
  "org.webjars.npm" % "radix-ui__react-id" % "1.0.1",
  "org.webjars.npm" % "radix-ui__react-menu" % "2.0.6",
  "org.webjars.npm" % "radix-ui__react-popper" % "1.1.3",
  "org.webjars.npm" % "radix-ui__react-portal" % "1.0.4",
  "org.webjars.npm" % "radix-ui__react-presence" % "1.0.1",
  "org.webjars.npm" % "radix-ui__react-primitive" % "1.0.3",
  "org.webjars.npm" % "radix-ui__react-roving-focus" % "1.0.4",
  "org.webjars.npm" % "radix-ui__react-slot" % "1.0.2",
  "org.webjars.npm" % "radix-ui__react-tooltip" % "1.0.7",
  "org.webjars.npm" % "radix-ui__react-use-callback-ref" % "1.0.1",
  "org.webjars.npm" % "radix-ui__react-use-controllable-state" % "1.0.1",
  "org.webjars.npm" % "radix-ui__react-use-escape-keydown" % "1.0.3",
  "org.webjars.npm" % "radix-ui__react-use-layout-effect" % "1.0.1",
  "org.webjars.npm" % "radix-ui__react-use-rect" % "1.0.1",
  "org.webjars.npm" % "radix-ui__react-use-size" % "1.0.1",
  "org.webjars.npm" % "radix-ui__react-visually-hidden" % "1.0.3",
  "org.webjars.npm" % "radix-ui__rect" % "1.0.1",
  "org.webjars.npm" % "react-remove-scroll" % "2.5.5",
  "org.webjars.npm" % "react-remove-scroll-bar" % "2.3.4",
  "org.webjars.npm" % "react-style-singleton" % "2.2.1",
  "org.webjars.npm" % "regenerator-runtime" % "0.13.11",
  "org.webjars.npm" % "scheduler" % "0.23.0",
  "org.webjars.npm" % "set-value" % "4.1.0",
  "org.webjars.npm" % "style-value-types" % "5.0.0",
  "org.webjars.npm" % "toggle-selection" % "1.0.6",
  "org.webjars.npm" % "tslib" % "2.6.2",
  "org.webjars.npm" % "types__codemirror" % "5.60.15",
  "org.webjars.npm" % "types__tern" % "0.23.9",
  "org.webjars.npm" % "uc.micro" % "1.0.6",
  "org.webjars.npm" % "use-callback-ref" % "1.3.1",
  "org.webjars.npm" % "use-sidecar" % "1.1.2",
  "org.webjars.npm" % "vscode-languageserver-types" % "3.17.5",
)

// database

ThisBuild / libraryDependencies ++= {
  import SangriaExample._

  database.value match {
    case H2 =>
      Seq(
        "com.h2database" % "h2" % Versions.h2,
      )
  }
}

// Web API

libraryDependencies ++= {
  val akkaHttpVersion        = "10.5.3"
  val akkaVersion            = "2.8.5"
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
