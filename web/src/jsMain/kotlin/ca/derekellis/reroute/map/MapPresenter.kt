package ca.derekellis.reroute.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import ca.derekellis.reroute.data.DataSource
import ca.derekellis.reroute.db.DatabaseHelper
import ca.derekellis.reroute.ui.CollectEffect
import ca.derekellis.reroute.ui.Navigator
import ca.derekellis.reroute.ui.Presenter
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.dsl.feature
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import ca.derekellis.reroute.stops.Stop as StopScreen

@Inject
class MapPresenter(
  private val dataSource: DataSource,
  private val interactionsManager: MapInteractionsManager,
  private val navigator: Navigator,
  private val withDatabase: DatabaseHelper,
  private val args: Map,
) : Presenter<MapViewModel, MapViewEvent> {
  @Composable
  override fun produceModel(events: Flow<MapViewEvent>): MapViewModel {
    CollectEffect(events) { event ->
      when (event) {
        is StopClick -> navigator.goTo(StopScreen(event.code))
      }
    }

    var routeFeatures by remember { mutableStateOf<Set<Feature>>(emptySet()) }
    val displayedRoutes by interactionsManager.routeVariants.collectAsState()
    LaunchedEffect(displayedRoutes) {
      withDatabase { database ->
        if (displayedRoutes.isEmpty()) {
          routeFeatures = emptySet()
        } else {
          database.routeVariantQueries.getByIds(displayedRoutes).asFlow().mapToList(coroutineContext)
            .collect { routes ->
              routeFeatures = routes.mapIndexedTo(mutableSetOf()) { index, route ->
                feature(route.shape) {
                  put("id", route.id)
                  put("gtfsId", route.gtfsId)
                  put("color", routeColor(route.gtfsId.split("-").first()))
                }
              }
            }
        }
      }
    }

    val targetStop by interactionsManager.targetStop.collectAsState(null)

    return MapViewModel(targetStop, routeFeatures)
  }

  // TODO: Extract route colours into dataset
  private fun routeColor(identifier: String) = when (identifier) {
    "E1", "1" -> COLOR_CONFEDERATION
    "2" -> COLOR_TRILLIUM
    "6", "7", "10", "11", "12", "14", "25", "40", "44", "51", "53", "80", "85", "87", "88", "90", "111" -> COLOR_FREQUENT
    "39", "45", "57", "61", "62", "63", "74", "75", "97", "98", "99" -> COLOR_RAPID
    "221", "222", "228", "231", "232", "234", "236", "237", "252", "256", "257", "258", "261", "262", "263", "264", "265", "267", "268", "270", "271", "272", "273", "277", "278", "282", "283", "284", "290", "291", "294", "299" -> COLOR_CONNEXION
    else -> COLOR_LOCAL
  }

  companion object {
    private const val COLOR_LOCAL = "#404040"
    private const val COLOR_FREQUENT = "#f26532"
    private const val COLOR_RAPID = "#1a559b"
    private const val COLOR_CONNEXION = "#9b5ba4"
    private const val COLOR_CONFEDERATION = "#d62e3b"
    private const val COLOR_TRILLIUM = "#76bf43"
  }
}
