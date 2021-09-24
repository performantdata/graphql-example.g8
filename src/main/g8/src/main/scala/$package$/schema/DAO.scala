package com.performantdata.data.user.schema

import com.performantdata.data.user.domain.Tables
import com.performantdata.data.user.model._
import com.typesafe.scalalogging.LazyLogging
import slick.jdbc.H2Profile.api._

import scala.concurrent.{ExecutionContext, Future}

/** Our data access object, for access to the underlying data store. */
case class DAO private (db: Database)(implicit ec: ExecutionContext) extends LazyLogging {
  def getUserByName(name: String): Future[Option[User]] =
    db.run(Tables.User.filter(_.name === name).result.headOption)  // name should be unique
      .map(_.map(u => User(
        id = ID(u.id),
        name = u.name
      )))

  /** Return the users having the given IDs. */
  def users(ids: Seq[ID]): Future[Seq[User]] =
    db.run(Tables.User.filter(_.id inSet ids.map(_.id)).result)
      .map(_.map(u => User(
        id = ID(u.id),
        name = u.name
      )))
}

object DAO {
  /** Slick database access. */
  private[this] val database = Database.forConfig("sangria.database")

  /** Return one of our data access objects. */
  def apply()(implicit ec: ExecutionContext): DAO = new DAO(database)
}
