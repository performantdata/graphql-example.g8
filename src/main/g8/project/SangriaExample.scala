import sbt._

/** Configuration for building the Sangria example code. */
object SangriaExample {
  /** A Web API implementation. */
  sealed trait WebApi

  /** Akka HTTP. */
  case object AkkaHttp extends WebApi

  /** A database vendor. */
  sealed trait Database

  /** H2. */
  case object H2 extends Database

  val webApi = settingKey[WebApi]("Implementation to use for the Web API.")
  webApi := AkkaHttp

  val database = settingKey[Database]("Database vendor to use for persistence.")
  database := H2
}
