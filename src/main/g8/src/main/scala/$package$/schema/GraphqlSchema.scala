package $package$.schema

import better.files.Resource
import sangria.parser.QueryParser
import sangria.schema._
import $package$.schema.{SangriaContext => Ctx}
import sangria.ast.ScalarTypeDefinition

import java.nio.charset.Charset

/** The GraphQL schema for the user data service. */
object GraphqlSchema extends UserSchemaDirectives with ScalarTypes {
  /** The AST of our server's GraphQL schema. */
  private[this] val schemaAst = {
    // Load the user schema.
    val schema = Resource.getAsString("user.graphql")(Charset.forName("UTF-8"))
    QueryParser.parse(schema).fold(throw _, identity)
  }

  private[this] val builder = AstSchemaBuilder.resolverBased[Ctx](
    // resolve our custom scalar types
    ScalarResolver { case d => d.name match {
      case GraphqlPersonId.name => GraphqlPersonId
      case GraphqlUserId.name => GraphqlUserId
    }},

    DirectiveResolver(getUserByNameDirective, c => c.ctx.ctx.dao.getUserByName(c.ctx.args.arg("name"))),
    DirectiveResolver(createPersonDirective,
      c => c.ctx.ctx.dao.createPerson(c.ctx.args.arg("fullName"))),
    DirectiveResolver(createUserDirective,
      c => c.ctx.ctx.dao.createUser(c.ctx.args.arg("name"), c.ctx.args.arg("person"))),
    FieldResolver.defaultInput
  )

  val UserSchema: Schema[Ctx, Any] = Schema.buildFromAst(schemaAst, builder.validateSchemaWithException(schemaAst))
}
