package ca.derekellis.reroute.server

import ca.derekellis.reroute.server.data.DataRoute
import ca.derekellis.reroute.server.di.RerouteScope
import ca.derekellis.reroute.server.realtime.RealtimeRoute
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import java.time.Duration

@Inject
@RerouteScope
class RerouteServer(
  private val dataRoute: DataRoute,
  private val realtimeRoute: RealtimeRoute,
) {
  context(Application)
  fun ktorModule() {
    install(ContentNegotiation) {
      json()
    }
    install(AutoHeadResponse)
    install(WebSockets) {
      pingPeriod = Duration.ofSeconds(15)
      timeout = Duration.ofSeconds(15)
      maxFrameSize = Long.MAX_VALUE
      masking = false

      contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
    install(CallLogging)

    routing {
      dataRoute.route()
      realtimeRoute.route()
    }
  }
}
