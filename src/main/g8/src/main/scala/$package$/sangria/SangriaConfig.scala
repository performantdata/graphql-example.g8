package $package$.sangria

/** Configuration for the Sangria example code. */
case class SangriaConfig(database: Database)

/** Configuration for the Sangria database.
  *
  * @param url JDBC connection URL for the database
  */
case class Database(url: String)
