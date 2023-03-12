package ca.derekellis.reroute.models

import io.github.dellisd.spatialk.geojson.Position
import kotlinx.serialization.Serializable

@Serializable
data class Stop(
  val id: String,
  val code: String?,
  val name: String,
  val position: Position,
)
