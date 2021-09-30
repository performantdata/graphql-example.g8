package $package$.sangria

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/** Entry point for an Akka HTTP-based Sangria service. */
object AkkaHttpApp {
  /** Start the Web server.
    *
    * @param args ignored
    */
  def main(args: Array[String]): Unit = {
    LiquibaseUtilities.updateSchema()

    /** The root behavior, which will start our HTTP server. */
    val rootBehavior = Behaviors.setup[Nothing] { context =>
      new HttpServer()(context.system).start()
      Behaviors.empty
    }

    // Start the actor system with the root behavior.
    ActorSystem[Nothing](rootBehavior, "AkkaHttpServer")
  }

  private[this]
  class HttpServer(implicit system: ActorSystem[_]) extends AkkaRoutes()(system.executionContext) {
    import system.executionContext

    def start(): Unit = {
      val futureBinding = Http().newServerAt("0.0.0.0", 8080).bind(routes)
      futureBinding.onComplete {
        case Success(binding) =>
          val address = binding.localAddress
          system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)

        case Failure(ex) =>
          system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
          system.terminate()
      }
    }
  }
}
