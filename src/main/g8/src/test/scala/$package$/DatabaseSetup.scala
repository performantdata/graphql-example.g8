package $package$

import $package$.sangria.LiquibaseUtilities
import org.scalatest.{BeforeAndAfterAll, Suite}

import java.io.File
import java.nio.file.Files

/** Provide a database for a Scalatest suite.
  *
  * Uses the database settings from the Typesafe configuration.
  * Assumes that the configuration is for a local file-based database.
  * Creates an empty database schema before the tests, and deletes the disk files afterwards.
  */
trait DatabaseSetup extends BeforeAndAfterAll { this: Suite =>
  private[this] var dir: File = _

  override protected def beforeAll(): Unit = {
    dir = Files.createTempDirectory("$name;format="normalize"$").toFile
    val path = dir.getAbsolutePath
    System.setProperty("TemporaryDirectory", path)
    println(s"Database temporary is at \$path.")
    ConfigReloader.reload {
      LiquibaseUtilities.updateSchema()
    }

    super.beforeAll()
  }

  override protected def afterAll(): Unit =
    try super.afterAll()
    finally dir.delete()
}
