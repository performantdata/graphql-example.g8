package com.performantdata.data.user.schema

import better.files.Resource
import sangria.parser.QueryParser
import sangria.schema._

import java.nio.charset.Charset

/** The GraphQL schema for the user data service. */
object GraphqlSchema {
  private[this] val schemaAst = {
    // Load the user schema from the file in this package.
    val resourceName = this.getClass.getPackageName.replace('.', '/') + "/user.graphql"
    val utf8 = Charset.forName("UTF-8")
    val schema = Resource.getAsString(resourceName)(utf8)
    QueryParser.parse(schema).fold(throw _, identity)
  }

  private[this] val builder = AstSchemaBuilder.resolverBased[Any](
  )

  val UserSchema: Schema[Any, Any] = Schema.buildFromAst(schemaAst, builder.validateSchemaWithException(schemaAst))
}
