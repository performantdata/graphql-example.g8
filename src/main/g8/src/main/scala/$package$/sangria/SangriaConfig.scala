package $package$.sangria

/** Configuration for the Sangria example code. */
case class SangriaConfig(database: Database)

/** Configuration for the Sangria database.
  *
  * @param url      JDBC connection URL for the database
  * @param user     database username credential
  * @param password database password credential
  */
case class Database(url: String, user: Option[String], password: Option[String])
