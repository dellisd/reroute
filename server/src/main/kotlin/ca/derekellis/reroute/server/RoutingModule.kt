package ca.derekellis.reroute.server

import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing

interface RoutingModule {
  context(route: Routing)
  fun route(): Route
}
