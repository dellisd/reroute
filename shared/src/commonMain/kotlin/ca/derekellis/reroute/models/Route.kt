package ca.derekellis.reroute.models

import io.github.dellisd.spatialk.geojson.LineString
import kotlinx.serialization.Serializable

/**
 * A (simplified) representation of a transit route and its variants/branches.
 * A unique combination of [gtfsId] + [headsign] + [directionId] is considered a unique route variant.
 *
 * @param id A unique identifier for this route variation
 * @param gtfsId The id used to reference this route in GTFS data
 * @param name The display name of the route
 * @param headsign The destination of this variant of the route
 * @param directionId The GTFS direction ID associated with this route+headsign combo
 * @param weight The number of scheduled trips for this particular variant
 */
@Serializable
data class Route(
  val id: String,
  val gtfsId: String,
  val name: String,
  val headsign: String,
  val directionId: Int,
  val weight: Int,
  val shape: LineString,
)
