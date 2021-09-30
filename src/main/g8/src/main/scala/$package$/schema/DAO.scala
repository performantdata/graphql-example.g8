package $package$.schema

import $package$.domain.{PersonId, Tables => T, UserId}
import $package$.model._
import com.typesafe.scalalogging.LazyLogging
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/** Our data access object, for access to the underlying data store. */
case class DAO private (dc: DatabaseConfig[JdbcProfile])(implicit ec: ExecutionContext) extends LazyLogging {
  import dc.profile.api._
  private[this] val db = dc.db

  override def toString: String = s"\${getClass.getSimpleName}(\${dc.config})"

  /** Return the user having the given name.
    *
    * @return `None` if no user with this name exists.
    */
  def getUserByName(name: String): Future[Option[User]] =
    db.run(T.User.filter(_.name === name).result.headOption)  // name should be unique
      .map(_.map(u => User(id = u.id.value, name = u.name)))

  /** Create a person in the system.
    *
    * @param fullName The person's full name.
    * @return ID of the new person.
    */
  def createPerson(fullName: String): Future[PersonId] =
    db.run((T.Person.map(_.fullname) returning T.Person.map(_.id)) += fullName)

  /** Create a user.
    *
    * @param name The user's name. Must be unique within the system.
    * @param personId ID of the person who controls this user account.
    * @return ID of the new user.
    */
  def createUser(name: String, personId: PersonId): Future[UserId] =
    db.run((T.User.map(u => (u.name, u.person)) returning T.User.map(_.id)) += (name, personId))

  /** Return the users having the given IDs. */
  def users(ids: Seq[UserId]): Future[Seq[User]] =
    db.run(T.User.filter(_.id inSet ids).result)
      .map(_.map(u => User(id = u.id.value, name = u.name)))
}

object DAO {
  /** Return one of our data access objects. */
  def apply()(implicit ec: ExecutionContext): DAO = {
    /* For testing purposes, we re-fetch the config each construction, in case it has changed.
     * Hopefully, for production performance, it's being cached by Typesafe Config. */
    val databaseConfig = DatabaseConfig.forConfig[JdbcProfile]("sangria.database")
    new DAO(databaseConfig)
  }
}
