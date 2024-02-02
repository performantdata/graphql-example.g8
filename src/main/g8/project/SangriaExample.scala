import sbt._

/** Configuration for building the Sangria example code. */
object SangriaExample {
  /** Return the JDBC driver class name for the given database. */
  def jdbcDriver(db: Database): String = db match {
    case H2 => "org.h2.Driver"
  }

  /** Return the Slick profile class name for the given database. */
  def slickProfile(db: Database): String = db match {
    case H2 => "slick.jdbc.H2Profile"
  }

  /** A database vendor. */
  sealed trait Database

  /** H2. */
  case object H2 extends Database

  val database = settingKey[Database]("Database vendor to use for persistence.")
}
