/* Build script related to the Sangria example code.
 *
 * It should not be necessary for users of this template project to modify this code.
 */
val jacksonVersion = "2.12.5"
val log4jVersion   = "2.14.1"
val reactVersion   = "17.0.2"

libraryDependencies ++= Seq(
  "com.github.pureconfig" %% "pureconfig" % "0.16.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
  "org.liquibase" % "liquibase-core" % "4.4.3",

  // Log4J2
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
)

// Web API

val akkaHttpVersion        = "$akka_http_version$"
val akkaVersion            = "$akka_version$"
val sangriaAkkaHttpVersion = "0.0.2"

libraryDependencies ++= {
  import SangriaExample._

  webApi.value match {
    case AkkaHttp =>
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

// database

libraryDependencies ++= {
  import SangriaExample._

  database.value match {
    case H2 =>
      Seq(
        "com.h2database" % "h2" % "1.4.200",
      )
  }
}

enablePlugins(JavaAppPackaging)
