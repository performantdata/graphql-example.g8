package $package$.sangria

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.webjars.WebJarAssetLocator
import sangria.execution.deferred.DeferredResolver
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.http.akka.Util.explicitlyAccepts
import sangria.slowlog.SlowLog

import scala.util.{Failure, Success, Try}

// This is the trait that makes `prepareGraphQLRequest` available
import sangria.http.akka.circe.CirceHttpSupport

/** Entry point for an Akka HTTP-based Sangria service. */
object AkkaHttpApp extends CirceHttpSupport {
  /** Start the Web server.
    *
    * @param args ignored
    */
  def main(args: Array[String]): Unit = {
    LiquibaseUtilities.updateSchema()

    /** The root behavior, which will start our HTTP server. */
    val rootBehavior = Behaviors.setup[Nothing] { context =>
      startHttpServer()(context.system)
      Behaviors.empty
    }

    // Start the actor system with the root behavior.
    ActorSystem[Nothing](rootBehavior, "AkkaHttpServer")
  }

  private[this] def startHttpServer()(implicit system: ActorSystem[_]): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    import system.executionContext

    /** Routes for GraphQL. */
    val routes: Route =
      optionalHeaderValueByName("X-Apollo-Tracing") { tracing =>
        path("graphql") {
          graphiql ~
          prepareGraphQLRequest {
            case Success(req) =>
              val middleware = if (tracing.isDefined) SlowLog.apolloTracing :: Nil else Nil
              val deferredResolver = DeferredResolver.fetchers(SchemaDefinition.characters)
              val graphQLResponse = Executor.execute(
                schema           = SchemaDefinition.StarWarsSchema,
                queryAst         = req.query,
                userContext      = new CharacterRepo,
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
      } ~
      (get & pathEndOrSingleSlash) {
        redirect("/graphql", PermanentRedirect)
      } ~
      webJars

    val futureBinding = Http().newServerAt("localhost", 8080).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)

      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

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
}
