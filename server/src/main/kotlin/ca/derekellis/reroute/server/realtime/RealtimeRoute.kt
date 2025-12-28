package ca.derekellis.reroute.server.realtime

import ca.derekellis.reroute.server.RoutingModule
import ca.derekellis.reroute.server.di.RerouteScope
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Point
import org.slf4j.LoggerFactory
import java.time.Instant
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Inject
@RerouteScope
class RealtimeRoute(
  private val worker: OcTranspoWorker,
  private val client: OcTranspoClient,
  private val gtfsRealtimeClient: GtfsRealtimeClient,
) : RoutingModule {
  private val logger = LoggerFactory.getLogger(javaClass)

  private var vehiclesCollection: FeatureCollection<Point, JsonObject>? = null
  private var vehiclesTimestamp: Instant = Instant.MIN

  context(routing: Routing)
  override fun route(): Route = routing.route("/realtime") {
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

    get("/vehicles") {
      if (vehiclesTimestamp + 25.seconds.toJavaDuration() <= Instant.now()) {
        vehiclesTimestamp = Instant.now()
        vehiclesCollection = gtfsRealtimeClient.vehicles()
      }

      call.respond(vehiclesCollection!!)
    }
  }
}
