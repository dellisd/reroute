package ca.derekellis.reroute.server

import ca.derekellis.reroute.server.data.DataRoute
import ca.derekellis.reroute.server.di.RerouteScope
import ca.derekellis.reroute.server.realtime.RealtimeRoute
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.seconds

@Inject
@RerouteScope
class RerouteServer(
  private val dataRoute: DataRoute,
  private val realtimeRoute: RealtimeRoute,
) {
  context(app: Application)
  fun ktorModule() {
    app.install(ContentNegotiation) {
      json()
    }
    app.install(AutoHeadResponse)
    app.install(WebSockets) {
      pingPeriod = 15.seconds
      timeout = 15.seconds
      maxFrameSize = Long.MAX_VALUE
      masking = false

      contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
    app.install(CallLogging)

    app.routing {
      dataRoute.route()
      realtimeRoute.route()
    }
  }
}
