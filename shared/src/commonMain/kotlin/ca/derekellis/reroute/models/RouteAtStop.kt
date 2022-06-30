package ca.derekellis.reroute.models

import kotlinx.serialization.Serializable

/**
 * A mapping between a stop and a route variant that serves that stop.
 *
 * @param stopId Refers to the ID of a [Stop]
 * @param routeId Refers to the ID of a [Route], not to be confused with the route's [gtfsId][Route.gtfsId]
 * @param index The ordering in which this stop appears in this particular route variation
 */
@Serializable
data class RouteAtStop(
    val stopId: String,
    val routeId: String,
    val index: Int,
)
