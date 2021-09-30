package $package$

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.ContentTypes.`application/json`
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import $package$.sangria.AkkaRoutes
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.freespec.AnyFreeSpec
import io.circe._
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._

/** Tests of our GraphQL service, via Akka HTTP routing. */
class AkkaRoutesSpec extends AnyFreeSpec with ScalaFutures with ScalatestRouteTest with DatabaseSetup {
  // the Akka HTTP route testkit does not yet support a typed actor system (https://github.com/akka/akka-http/issues/2036)
  // so we have to adapt for now
  private lazy val testKit = ActorTestKit()
  private implicit def typedSystem: ActorSystem[Nothing] = testKit.system
  override def createActorSystem(): akka.actor.ActorSystem = testKit.system.classicSystem

  "$name$" - {
    /** The Akka HTTP routes that we're testing. */
    val routes = new AkkaRoutes {}.routes

    /** HTTP POST to the GraphQL endpoint. */
    val post = Post(uri = "/graphql")

    "when its data store is empty" - {
      "can create a person in the system" in {
        val query = """mutation { createPerson(fullName: "Joe") }"""
        val entity = Marshal(GraphQLRequest(query)).to[RequestEntity].futureValue
          .withContentType(`application/json`)

        post.withEntity(entity) ~> routes ~> check {
          assert(status == StatusCodes.OK)
          assert(contentType == `application/json`)
          val response = entityAs[GraphqlResponse]
          assert(response.data == Map("createPerson" -> 1).asJson)
        }
      }

      "can create a user in the system" in {
        val query = """mutation { createUser(name: "joey", person: 1) }"""
        val entity = Marshal(GraphQLRequest(query)).to[RequestEntity].futureValue
          .withContentType(`application/json`)

        post.withEntity(entity) ~> routes ~> check {
          assert(status == StatusCodes.OK)
          assert(contentType == `application/json`)
          val response = entityAs[GraphqlResponse]
          assert(response.data == Map("createUser" -> 1).asJson)
        }
      }

      "can retrieve all users" in {
        val query = """{ username(name: "joey") { id name person { id fullName } } }"""
        val entity = Marshal(GraphQLRequest(query)).to[RequestEntity].futureValue
          .withContentType(`application/json`)

        post.withEntity(entity) ~> routes ~> check {
          assert(status == StatusCodes.OK)
          assert(contentType == `application/json`)
          val response = entityAs[GraphqlResponse]
          assert(response.data == Map("username" -> Map("id" -> 1)).asJson)
        }
      }

      "can retrieve a user by name" in {
        val query = """{ username(name: "joey") { id name person { id fullName } } }"""
        val entity = Marshal(GraphQLRequest(query)).to[RequestEntity].futureValue
          .withContentType(`application/json`)

        post.withEntity(entity) ~> routes ~> check {
          assert(status == StatusCodes.OK)
          assert(contentType == `application/json`)
          val response = entityAs[GraphqlResponse]
          assert(response.data == Map("username" -> Map("id" -> 1)).asJson)
        }
      }
    }
  }
}
