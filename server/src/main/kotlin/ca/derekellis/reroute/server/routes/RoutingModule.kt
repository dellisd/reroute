package ca.derekellis.reroute.server.routes

import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing

interface RoutingModule {
    context(Routing)
    fun route(): Route
}
