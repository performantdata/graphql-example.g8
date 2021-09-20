package $package$.sangria

/** Configuration for the Sangria example code. */
case class SangriaConfig(database: Database)

/** Configuration for the Sangria database.
  *
  * @param url      JDBC connection URL for the database
  * @param driver   classname of the JDBC driver to use
  * @param user     database username credential
  * @param password database password credential
  */
case class Database(url: String, driver: String, user: Option[String], password: Option[String])
