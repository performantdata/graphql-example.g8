import slick.ast.ColumnOption
import slick.codegen.SourceCodeGenerator
import slick.{model => m}
import slick.model.Model

class SangriaExampleCodeGenerator(model: Model) extends SourceCodeGenerator(model) {
  import SangriaExampleCodeGenerator._

  // copied from Slick 3.3.3 parent
  private[this] def handleQuotedNamed(tableName: String) =
    if (tableName.endsWith("`")) s"\${tableName.init}Table`" else s"\${tableName}Table"

  // Copied from Slick 3.3.3 parent, modified to remove early initialization.
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

  // Copied from Slick 3.3.3 parent, modified for PK value classes.
  override def packageTableCode(tableName: String, tableCode: String, pkg: String, container: String): String = {
    val tableIdCode = codePerTableId(tableName)
    s"""
package \${pkg}
// AUTO-GENERATED Slick data model for table \${tableName}
\$tableIdCode
trait \${handleQuotedNamed(tableName) } {

  self:\${container}  =>

  import profile.api._
  \${indent(tableCode)}
}
      """.trim()
  }


  /** Generates a map that associates the table name with the generated code for the table's PK value class.
    * (not wrapped in a package yet).
    */
  private[this] def codePerTableId: Map[String,String] =
    tables.map(t => (t.TableValue.name, t.asInstanceOf[TableDef].idCode)).toMap

  override def Table: m.Table => Table = new TableDef(_)

  class TableDef(model: m.Table) extends super.TableDef(model) {
    private[this] lazy val tableModel = model

    /** The table's FKs that reference a PK column that uses an ID class. */
    private[this] lazy val ourForeignKeys = tableModel.foreignKeys.filter(_.referencedColumns.count(usesIdClass) == 1)

    def idCode: String = {
      val idColumns = model.columns.filter(usesIdClass)
      assert(idColumns.size <= 1, s"Table \${model.name.table} has \${idColumns.size} primary key id columns.")

      idColumns.headOption
        .map { c =>
          val originalExposedType = super.Column(c).exposedType
          val exposedType = Column(c).exposedType
          s"""import slick.lifted.MappedTo
             |
             |/** Primary key type for table \${TableValue.name}. */
             |case class \$exposedType(value: \$originalExposedType) extends AnyVal with MappedTo[\$originalExposedType]
             |""".stripMargin
        }
        .getOrElse("")
    }

    /** @inheritdoc
      *
      * Prepends to the usual definitions a case value class definition,
      * if the table has [[SangriaExampleCodeGenerator#usesIdClass a primary key identifier column]].
      * The value class will be used as the type of the PK column elsewhere.
      */
/*
    override def definitions: Seq[Def] = {
      val idColumns = model.columns.filter(usesIdClass)
      assert(idColumns.size <= 1, s"Table \${model.name.table} has \${idColumns.size} primary key id columns.")
      idColumns.headOption
        .map { c =>
          val originalExposedType = super.Column(c).exposedType
          val exposedType = Column(c).exposedType
          val IdType = new TypeDef {
            override def doc: String = s"Primary key type for table \${TableValue.name}.\n"

            override def code: String = {
              val prns = parents.map(" with " + _).mkString("")
              s"""case class \$exposedType(value: \$originalExposedType) extends AnyVal\$prns\n"""
            }

            override def rawName: String = tableName(model.name.table)
            override def parents: Seq[String] = Seq(s"MappedTo[\$originalExposedType]")
          }

          IdType +: super.definitions
        }
        .getOrElse(super.definitions)
    }
*/

    override def Column: m.Column => Column = new Column(_) {
      /** Return the name of the column's Scala type.
        *
        * @return
        *   If the column is the sole column of a primary key named "id",
        *   the name of a table-specific primary key type, generated elsewhere;
        *   otherwise the value from the standard Slick superclass.
        */
      override def rawType: String =
        if (usesIdClass(model))
          s"\${tableName(tableModel.name.table)}Id"
        else {
          /** FKs that have this column referencing a PK column that uses an ID class. */
          val fks = ourForeignKeys.filter(_.referencingColumns.contains(model))
          assert(fks.size <= 1,
            s"Column \${model.name} is used in \${fks.size} foreign key references. " +
              "We only support columns that are used in no more than one foreign key.")

          fks.headOption
            /*FIXME I wanted to use the referenced column's exported type name,
             * but I don't see how to wrap it in its `Table`'s `Column`. Is it the same `Column` as ours? */
            .map(fk => s"\${tableName(fk.referencedTable.table)}Id")
            .getOrElse(super.rawType)
        }
    }
  }
}

object SangriaExampleCodeGenerator {
  /** Return whether the given column makes use of an identifier class. */
  private def usesIdClass(col: m.Column): Boolean =
    col.name.toLowerCase == "id" &&
      col.options.contains(ColumnOption.PrimaryKey)
}
