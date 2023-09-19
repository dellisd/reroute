package ca.derekellis.reroute.models

import io.github.dellisd.spatialk.geojson.LineString
import kotlinx.serialization.Serializable

/**
 * A variant/branch of a [Route].
 *
 * @param id A unique id for the route variation
 * @param gtfsId The GTFS id of the route
 * @param directionId The GTFS direction id for trips on this route variant
 * @param headsign The destination of this particular variant of the route
 * @param weight The number of trips on this variant, used to weight different variants against each other.
 * @param shape The GeoJSON shape of this variant.
 */
@Serializable
data class RouteVariant(
  val id: String,
  val gtfsId: String,
  val directionId: Int,
  val headsign: String,
  val weight: Int,
  val shape: LineString,
)
