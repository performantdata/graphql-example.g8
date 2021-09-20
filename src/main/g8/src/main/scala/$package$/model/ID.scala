package com.performantdata.data.user.model

/** An identifier of an element in one of our database tables.
  *
  * @param id The identifier. Must be unique within its containing table.
  */
case class ID(id: Long) extends AnyVal
