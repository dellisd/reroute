package ca.derekellis.reroute.map

import ca.derekellis.reroute.models.Stop
import geojson.FeatureCollection

data class MapViewModel(
  val targetStop: Stop?,
  val featureCollection: FeatureCollection,
)
