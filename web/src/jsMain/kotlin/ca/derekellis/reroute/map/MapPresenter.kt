package ca.derekellis.reroute.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import ca.derekellis.reroute.data.DataSource
import ca.derekellis.reroute.models.Stop
import ca.derekellis.reroute.ui.CollectEffect
import ca.derekellis.reroute.ui.Navigator
import ca.derekellis.reroute.ui.Presenter
import ca.derekellis.reroute.utils.jsObject
import geojson.Feature
import geojson.FeatureCollection
import geojson.Point
import geojson.Position
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import kotlin.js.Json
import ca.derekellis.reroute.stops.Stop as StopScreen

@Inject
class MapPresenter(
  private val dataSource: DataSource,
  private val interactionsManager: MapInteractionsManager,
  private val navigator: Navigator,
  private val args: Map,
) :
  Presenter<MapViewModel, MapViewEvent> {
  @Composable
  override fun produceModel(events: Flow<MapViewEvent>): MapViewModel {
    CollectEffect(events) { event ->
      when (event) {
        is StopClick -> navigator.goTo(StopScreen(event.code))
      }
    }

    val targetStop by interactionsManager.targetStop.collectAsState(null)

    val stopsList by dataSource.getStops().collectAsState(emptyList())
    val features = remember(stopsList) {
      jsObject<FeatureCollection> {
        type = "FeatureCollection"
        this.features = Array(stopsList.size) { index -> stopsList[index].toFeature() }
      }
    }

    return MapViewModel(targetStop, features)
  }

  private fun Stop.toFeature(): Feature = jsObject {
    type = "Feature"
    geometry = jsObject<Point> {
      type = "Point"
      coordinates = position.coordinates.toTypedArray().unsafeCast<Position>()
      properties = jsObject<dynamic> {
        this.name = name
        this.code = code
        this.id = id
      }.unsafeCast<Json?>()
    }
  }
}
