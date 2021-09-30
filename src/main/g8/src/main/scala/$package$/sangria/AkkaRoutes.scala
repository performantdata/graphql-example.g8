package $package$.sangria

import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import $package$.schema.{GraphqlSchema, SangriaContext}
import org.webjars.WebJarAssetLocator
import sangria.execution.deferred.DeferredResolver
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.http.akka.Util.explicitlyAccepts
import sangria.marshalling.circe._
import sangria.slowlog.SlowLog

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

// This is the trait that makes `prepareGraphQLRequest` available
import sangria.http.akka.circe.CirceHttpSupport

/** Mix-in trait that contains the Akka HTTP routing.
  *
  * Separated from the HTTP server class for testing purposes.
  */
abstract class AkkaRoutes()(implicit ec: ExecutionContext) extends CirceHttpSupport {
  /** Tool for locating WebJar resources in the classpath. */
  private[this] val webJarAssetLocator = new WebJarAssetLocator()

  /** Route for WebJar assets.
    *
    * Tries to resolve the unmatched path with WebJar resources that are on the classpath.
    * Completes if so, rejects if it fails to find a unique asset with the path name.
    */
  private[this] val webJars: Route = extractUnmatchedPath { path =>
    Try(webJarAssetLocator.getFullPath(path.toString)) match {
      case Success(fullPath) => getFromResource(fullPath)
      case Failure(_: IllegalArgumentException) => reject
      case Failure(e) => failWith(e)
    }
  }

  /** Route for our GraphiQL page. */
  private[this] val graphiql: Route =  get {
    explicitlyAccepts(`text/html`) {
      // This asset needs to be provided in the classpath; it's not in any WebJar.
      getFromResource("assets/graphiql.html")
    }
  }

  /** Routes for GraphQL. */
  val routes: Route =
    optionalHeaderValueByName("X-Apollo-Tracing") { tracing =>
      path("graphql") {
        encodeResponse {
          graphiql ~
            prepareGraphQLRequest {
              case Success(req) =>
                val middleware = if (tracing.isDefined) SlowLog.apolloTracing :: Nil else Nil
                val deferredResolver: DeferredResolver[Any] = DeferredResolver.empty
                val graphQLResponse = Executor.execute(
                  schema           = GraphqlSchema.UserSchema,
                  queryAst         = req.query,
                  userContext      = SangriaContext(),
                  variables        = req.variables,
                  operationName    = req.operationName,
                  middleware       = middleware,
                  deferredResolver = deferredResolver
                ).map(OK -> _)
                  .recover {
                    case error: QueryAnalysisError => BadRequest -> error.resolveError
                    case error: ErrorWithResolver => InternalServerError -> error.resolveError
                  }
                complete(graphQLResponse)

              case Failure(preparationError) =>
                complete(BadRequest, formatError(preparationError))
            }
        }
      }
    } ~
    (get & pathEndOrSingleSlash) {
      redirect("/graphql", PermanentRedirect)
    } ~
    webJars
}
