package ca.derekellis.reroute.server

import ca.derekellis.reroute.server.data.DataRoute
import ca.derekellis.reroute.server.di.RerouteScope
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import me.tatarka.inject.annotations.Inject

@Inject
@RerouteScope
class RerouteServer(private val dataRoute: DataRoute) {
  context(Application)
  fun ktorModule() {
    install(ContentNegotiation) {
      json()
    }
    install(AutoHeadResponse)

    routing {
      dataRoute.route()
    }
  }
}
