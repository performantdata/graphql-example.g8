package $package$.schema

import $package$.domain.{PersonId, UserId}
import sangria.ast.{BigIntValue, IntValue, StringValue, Value}
import sangria.schema.ScalarType
import sangria.validation.Violation

import scala.util.Try

/** Mix-in trait for definitions of scalar types to add to our GraphQL schema.
  *
  * @see [[https://www.howtographql.com/graphql-scala/5-custom-scalars/]]
  */
trait ScalarTypes {
  import ScalarTypes._

  protected implicit val GraphqlPersonId: ScalarType[PersonId] = ScalarType[PersonId](name = "PersonId",
    description = Some("Unique identifier for a person in our system."),
    coerceUserInput = anyToLong andThen optionToPersonId,
    coerceOutput = (id, _) => id.value,
    coerceInput = valueToLong andThen optionToPersonId
  )

  protected implicit val GraphqlUserId: ScalarType[UserId] = ScalarType[UserId](name = "UserId",
    description = Some("Unique identifier for a user in our system."),
    coerceUserInput = anyToLong andThen optionToUserId,
    coerceOutput = (id, _) => id.value,
    coerceInput = valueToLong andThen optionToUserId
  )
}

object ScalarTypes {
  /** Validation violation specific to parsing identifiers. */
  case object IdentifierViolation extends Violation {
    override def errorMessage: String = "Error while parsing identifier."
  }

  /** Returns either the given identifier, wrapped, or a Sangria violation if conversion wasn't possible. */
  private def optionToPersonId(id: Option[Long]): Either[Violation, PersonId] =
    id.flatMap(id => Try(PersonId(id)).toOption).toRight(IdentifierViolation)

  /** Returns either the given identifier, wrapped, or a Sangria violation if conversion wasn't possible. */
  private def optionToUserId(id: Option[Long]): Either[Violation, UserId] =
    id.flatMap(id => Try(UserId(id)).toOption).toRight(IdentifierViolation)

  /** Returns a `Long` if the given value can be precisely converted to one. */
  private val anyToLong: Any => Option[Long] = {
    case l: Long => Some(l)
    case i: Int => Some(i)
    case bigInt: BigInt if bigInt.isValidLong => Some(bigInt.longValue)
    case bigDecimal: BigDecimal if bigDecimal.isValidLong => Some(bigDecimal.longValue)
    case str: String => Try(str.toLong).toOption
    case _ => None
  }

  /** Returns a `Long` if the given GraphQL AST value can be precisely converted to one. */
  private val valueToLong: Value => Option[Long] = {
    case IntValue(i, _, _) => Some(i)
    case BigIntValue(bigInt, _, _) if bigInt.isValidLong => Some(bigInt.longValue)
    case StringValue(str, _, _, _, _) => Try(str.toLong).toOption
    case _ => None
  }
}
