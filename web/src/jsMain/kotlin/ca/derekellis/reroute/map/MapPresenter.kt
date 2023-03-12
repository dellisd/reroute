package ca.derekellis.reroute.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ca.derekellis.reroute.data.DataSource
import ca.derekellis.reroute.ui.CollectEffect
import ca.derekellis.reroute.ui.Navigator
import ca.derekellis.reroute.ui.Presenter
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
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

    return MapViewModel(targetStop)
  }
}
