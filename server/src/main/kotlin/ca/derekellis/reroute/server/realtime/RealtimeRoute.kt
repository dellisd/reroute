package ca.derekellis.reroute.server.realtime

import ca.derekellis.reroute.server.RoutingModule
import ca.derekellis.reroute.server.di.RerouteScope
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import me.tatarka.inject.annotations.Inject
import org.slf4j.LoggerFactory

@Inject
@RerouteScope
class RealtimeRoute(
  private val worker: OcTranspoWorker,
  private val client: OcTranspoClient,
) : RoutingModule {
  private val logger = LoggerFactory.getLogger(javaClass)

  context(Routing)
  override fun route(): Route = route("/realtime") {
    /*webSocket("/{code}") {
      val code = call.parameters["code"]!!
      logger.info("WebSocket opened for: {}", code)

      try {
        worker.subscribe(code) {
          sendSerialized(it)
        }
      } catch (e: ClosedReceiveChannelException) {
        logger.info("Connection closed on {}.", code)
      } catch (e: Exception) {
        logger.error("Internal error", e)
        close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, ""))
      }
    }*/

    get("/{code}") {
      val code = call.parameters["code"]!!
      val result = client.get(code)

      call.respond(result)
    }
  }
}
