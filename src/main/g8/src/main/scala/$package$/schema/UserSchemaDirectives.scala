package $package$.schema

import sangria.schema.{Directive, DirectiveLocation}

/** Definitions of internally-used GraphQL schema directives.
  *
  * The GraphQL language provides for the definition of [[sangria.ast.DirectiveDefinition directives]],
  * which can be applied to other GraphQL schema elements.
  * We use directives for marking up our service's schema,
  * so that we can locate fields that require special resolvers (and then provide those resolvers).
  * We don't, however, define those directives in the schema itself;
  * we define them here in Scala in this mix-in trait.
  * This is because the directives are only used internally by the server
  * and aren't intended to be used by the clients.
  */
trait UserSchemaDirectives {
  protected val getUserByNameDirective: Directive =
    Directive("getUserByName", locations = Set(DirectiveLocation.FieldDefinition))
  protected val createPersonDirective: Directive =
    Directive("createPerson", locations = Set(DirectiveLocation.FieldDefinition))
  protected val createUserDirective: Directive =
    Directive("createUser", locations = Set(DirectiveLocation.FieldDefinition))
}
