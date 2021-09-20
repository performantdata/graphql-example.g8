package com.performantdata.data.user.schema

import better.files.Resource
import sangria.parser.QueryParser
import sangria.schema._

import java.nio.charset.Charset

/** The GraphQL schema for the user data service. */
object GraphqlSchema {
  private[this] val schemaAst = {
    // Load the user schema.
    val schema = Resource.getAsString("user.graphql")(Charset.forName("UTF-8"))
    QueryParser.parse(schema).fold(throw _, identity)
  }

  private[this] val builder = AstSchemaBuilder.resolverBased[Any](
  )

  val UserSchema: Schema[Any, Any] = Schema.buildFromAst(schemaAst, builder.validateSchemaWithException(schemaAst))
}
