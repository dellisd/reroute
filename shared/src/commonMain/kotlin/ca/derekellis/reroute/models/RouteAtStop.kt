package ca.derekellis.reroute.models

import kotlinx.serialization.Serializable

/**
 * A mapping between a stop and a route that serves that stop.
 *
 * @param stopId Refers to the ID of a [Stop]
 * @param routeId Refers to the ID of a [Route], not to be confused with the route's [gtfsId][Route.gtfsId]
 */
@Serializable
data class RouteAtStop(
    val stopId: String,
    val routeId: String,
)
