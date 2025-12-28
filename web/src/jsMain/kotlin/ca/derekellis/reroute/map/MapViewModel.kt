package ca.derekellis.reroute.map

import ca.derekellis.reroute.models.Stop
import kotlinx.serialization.json.JsonObject
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.LineString
import org.maplibre.spatialk.geojson.Point

data class MapViewModel(
  val targetStop: Stop?,
  val routeFeatures: Set<Feature<LineString, JsonObject>>,
  val vehicles: FeatureCollection<Point, JsonObject>,
)
