package ca.derekellis.reroute.stops

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import ca.derekellis.reroute.data.DataSource
import ca.derekellis.reroute.home.Home
import ca.derekellis.reroute.map.MapInteractionsManager
import ca.derekellis.reroute.ui.CollectEffect
import ca.derekellis.reroute.ui.Navigator
import ca.derekellis.reroute.ui.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class StopPresenter(
  private val dataSource: DataSource,
  private val navigator: Navigator,
  private val mapInteractionsManager: MapInteractionsManager,
  private val args: Stop,
) : Presenter<StopViewModel, StopViewEvent> {
  @Composable
  override fun produceModel(events: Flow<StopViewEvent>): StopViewModel {
    CollectEffect(events) { event ->
      when (event) {
        Close -> {
          launch {
            mapInteractionsManager.goTo(null)
            navigator.goTo(Home)
          }
        }
      }
    }

    val routesAtStop by dataSource.getRoutesAtStop(args.code).collectAsState(initial = null)
    val stopList by dataSource.getStopByCode(args.code).collectAsState(initial = null)

    if (routesAtStop == null || stopList == null) return StopViewModel.Loading

    val routeSections = remember(routesAtStop) {
      (routesAtStop ?: emptyList()).map {
        StopViewModel.Loaded.RouteSection(
          it.gtfsId,
          it.identifier,
          it.destinations[it.directionId],
          it.directionId,
        )
      }.sortedWith(compareBy<StopViewModel.Loaded.RouteSection> { it.identifier.length }.thenBy(naturalOrder()) { it.identifier })
    }

    val stop = stopList!!.firstOrNull() ?: return StopViewModel.NotFound

    LaunchedEffect(stop) {
      mapInteractionsManager.goTo(stop)
    }

    return StopViewModel.Loaded(stop, routeSections)
  }
}
