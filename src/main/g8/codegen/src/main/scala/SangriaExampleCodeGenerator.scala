import slick.codegen.SourceCodeGenerator
import slick.model.Model

class SangriaExampleCodeGenerator(model: Model) extends SourceCodeGenerator(model) {
  // copied from parent
  private[this] def handleQuotedNamed(tableName: String) =
    if (tableName.endsWith("`")) s"\${tableName.init}Table`" else s"\${tableName}Table"

  // Copied from parent, modified to remove early initialization.
  override def packageContainerCode(profile: String, pkg: String, container: String = "Tables"): String = {
    val mixinCode = codePerTable.keys.map(tableName => s"\${handleQuotedNamed(tableName) }").mkString("extends ", " with ", "")
    s"""
package \${pkg}
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object \${container} extends Abstract\${container}(\$profile)

abstract class Abstract\${container}(val profile: slick.jdbc.JdbcProfile) extends \$container

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.)
    Each generated XXXXTable trait is mixed in this trait hence allowing access to all the TableQuery lazy vals.
  */
trait \${container}\${parentType.map(t => s" extends \$t").getOrElse("")} \${mixinCode} {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  \${indent(codeForContainer)}

}
      """.trim()
  }
}
