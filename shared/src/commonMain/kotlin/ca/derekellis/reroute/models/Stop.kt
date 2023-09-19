package ca.derekellis.reroute.models

import io.github.dellisd.spatialk.geojson.Position
import kotlinx.serialization.Serializable

/**
 * A stop or station in a transit system.
 *
 * @param id A unique identifier for this stop, typically corresponding to the id in the GTFS stops table.
 * @param code The code visible to users to reference this stop. If the original GTFS stops table does not contain a
 * code for the stop, the value of this parameter will be equal to [id].
 * @param name The name of the stop or station.
 * @param position The Lng Lat position of the stop.
 * @param parent A station could have multiple platforms where each [Stop] represents one of the station's platforms.
 * In this case, each platform's parent would be a [Stop] entry for the station as a whole.
 */
@Serializable
data class Stop(
  val id: String,
  val code: String,
  val name: String,
  val position: Position,
  val parent: String? = null,
)
