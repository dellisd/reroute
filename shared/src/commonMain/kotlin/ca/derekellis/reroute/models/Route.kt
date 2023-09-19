package ca.derekellis.reroute.models

import kotlinx.serialization.Serializable

/**
 * A transit route in a given direction. A route can have multiple [variants][RouteVariant].
 *
 * @param gtfsId The id used to reference this route in GTFS data
 * @param identifier The identifier of the route shown to users, typically a route number
 * @param destinations The destinations of the different directions for this route. The index in the list corresponds
 * to the directionId property in GTFS trips.
 */
@Serializable
data class Route(
  val gtfsId: String,
  val identifier: String,
  val destinations: List<String>,
)
