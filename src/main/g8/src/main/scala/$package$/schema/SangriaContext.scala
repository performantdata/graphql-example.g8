package $package$.schema

import scala.concurrent.ExecutionContext

/** Our service's context object to be passed through Sangria.
  *
  * In order to facilitate our fulfillment of GraphQL requests, Sangria allows an arbitrary "context" object to be
  * passed through with each request.
  * The context is a parameter to all of the Sangria schema definition classes.
  *
  * A typical use for this object is providing access to the data store.
  * While we could make this object itself a DAO, we instead enclose the DAO inside it,
  * anticipating the future addition of other non-DAO members to the class.
  */
case class SangriaContext()(implicit ec: ExecutionContext) {
  val dao: DAO = DAO()

  override def toString: String = s"\${getClass.getSimpleName}(\$dao)"
}
