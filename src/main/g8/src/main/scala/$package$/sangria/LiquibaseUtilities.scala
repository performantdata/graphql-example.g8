package $package$.sangria

import com.typesafe.scalalogging.StrictLogging
import liquibase.{Contexts, LabelExpression, Liquibase}
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor

import java.sql.DriverManager

/** Liquibase utilities. */
object LiquibaseUtilities extends StrictLogging {
  import pureconfig._
  import pureconfig.generic.auto._

  /** Update the schema of the connected database to the latest version.
    *
    * @see https://www.liquibase.org/blog/3-ways-to-run-liquibase
    */
  def updateSchema(): Unit = {
    val config = ConfigSource.default.at("sangria").loadOrThrow[SangriaConfig]
    val url = config.database.db.url

    logger.info(s"Updating database schema at \$url...")
    val connection = DriverManager.getConnection(url)
    try {
      val database = DatabaseFactory.getInstance.findCorrectDatabaseImplementation(new JdbcConnection(connection))
      val liquibase = new Liquibase("liquibase.xml", new ClassLoaderResourceAccessor(), database)
      try {
        liquibase.update(new Contexts(), new LabelExpression())
      } finally {
        liquibase.forceReleaseLocks()
      }
    } finally {
      connection.rollback()
      connection.close()
    }
    logger.info(s"Database schema update completed.")
  }
}
