package ca.derekellis.reroute.map

import ca.derekellis.reroute.models.Stop
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection

data class MapViewModel(
  val targetStop: Stop?,
  val routeFeatures: Set<Feature>,
  val vehicles: FeatureCollection,
)
