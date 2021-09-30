package com.performantdata.data

/** A data service for user data.
  *
  * The data is represented at three levels:
  *  - [[domain The `domain` types]] are generated from the database schema and represent the tables there.
  *  - [[model The `model` types]] represent the data presented across the GraphQL API.
  *  - [[schema The `schema` types]] represent the GraphQL schema that this service presents.
  *    The `schema` types wrap the `model` types, providing for their retrieval.
  *
  * === identifiers ===
  * Record identifiers are given table-specific types:
  * for example, the [[model.User User record]]'s identifier is of type [[model.UserId UserId]].
  * This prevents mistakes of passing one type of ID where another is expected.
  */
package object user {}
