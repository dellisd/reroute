package ca.derekellis.reroute.map

import ca.derekellis.reroute.models.Stop
import io.github.dellisd.spatialk.geojson.Feature

data class MapViewModel(
  val targetStop: Stop?,
  val routeFeatures: Set<Feature>,
)
