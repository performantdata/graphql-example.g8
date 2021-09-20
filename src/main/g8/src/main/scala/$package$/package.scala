package com.performantdata.data

/** A data service for user data.
  *
  * The data is represented at three levels:
  *  - [[domain The `domain` types]] are generated from the database schema and represent the tables there.
  *  - [[model The `model` types]] represent the data presented across the GraphQL API.
  *  - [[schema The `schema` types]] represent the GraphQL schema that this service presents.
  *    The `schema` types wrap the `model` types, providing for their retrieval.
  */
package object user {}
