package ca.derekellis.reroute.server.realtime

import ca.derekellis.reroute.server.config.LoadedServerConfig
import ca.derekellis.reroute.server.di.RerouteScope
import com.google.transit.realtime.FeedMessage
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.dsl.featureCollection
import io.github.dellisd.spatialk.geojson.dsl.point
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpHeaders
import me.tatarka.inject.annotations.Inject
import org.slf4j.LoggerFactory

@Inject
@RerouteScope
class GtfsRealtimeClient(
  private val config: LoadedServerConfig,
  private val httpClient: HttpClient,
) {
  private val logger = LoggerFactory.getLogger(GtfsRealtimeClient::class.java)
  suspend fun vehicles(): FeatureCollection {
    val credentials = requireNotNull(config.ocTranspo) { "No OC Transpo API credentials loaded" }

    logger.info("Fetching new Vehicle Positions feed")
    val response = httpClient.get("https://nextrip-public-api.azure-api.net/octranspo/gtfs-rt-vp/beta/v1/VehiclePositions") {
      headers {
        set("Ocp-Apim-Subscription-Key", credentials.realtimeApiKey)
        set(HttpHeaders.CacheControl, "no-cache")
      }
    }

    val message = FeedMessage.ADAPTER.decode(response.readBytes())
    requireNotNull(message.entity)

    return featureCollection {
      message.entity.mapNotNull { it.vehicle }
        .forEach { vehicleEntity ->
          val descriptor = requireNotNull(vehicleEntity.vehicle)
          val position = requireNotNull(vehicleEntity.position)

          feature(point(position.longitude.toDouble(), position.latitude.toDouble())) {
            put("id", descriptor.id ?: "unknown")
          }
        }
    }
  }
}
