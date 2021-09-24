package com.performantdata.data.user.schema

import better.files.Resource
import sangria.parser.QueryParser
import sangria.schema._
import com.performantdata.data.user.schema.{SangriaContext => Ctx}

import java.nio.charset.Charset

/** The GraphQL schema for the user data service. */
object GraphqlSchema extends UserSchemaDirectives {
  /** The AST of our server's GraphQL schema. */
  private[this] val schemaAst = {
    // Load the user schema.
    val schema = Resource.getAsString("user.graphql")(Charset.forName("UTF-8"))
    QueryParser.parse(schema).fold(throw _, identity)
  }

  private[this] val builder = AstSchemaBuilder.resolverBased[Ctx](
    DirectiveResolver(getUserByNameDirective, c => c.ctx.ctx.dao.getUserByName(c.ctx.args.arg("name"))),
    FieldResolver.defaultInput
  )

  val UserSchema: Schema[Ctx, Any] = Schema.buildFromAst(schemaAst, builder.validateSchemaWithException(schemaAst))
}
