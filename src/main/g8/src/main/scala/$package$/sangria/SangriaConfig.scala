package $package$.sangria

/** Configuration for the Sangria example code.
  *
  * @param databaseDirectory Absolute path of the directory in which test database(s) will be stored.
  */
case class SangriaConfig(databaseDirectory: Option[String], database: Database)

case class Database(db: DatabaseParameters)

/** Configuration for the Sangria database.
  *
  * @param url      JDBC connection URL for the database
  * @param driver   classname of the JDBC driver to use
  * @param user     database username credential
  * @param password database password credential
  */
case class DatabaseParameters(url: String, driver: String, user: Option[String], password: Option[String])
