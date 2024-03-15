package ca.derekellis.reroute.stops

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import ca.derekellis.reroute.data.DataSource
import ca.derekellis.reroute.data.RerouteClient
import ca.derekellis.reroute.home.Home
import ca.derekellis.reroute.map.MapInteractionsManager
import ca.derekellis.reroute.realtime.RealtimeMessage
import ca.derekellis.reroute.ui.CollectEffect
import ca.derekellis.reroute.ui.Navigator
import ca.derekellis.reroute.ui.Presenter
import ca.derekellis.reroute.utils.whenWindowFocused
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.seconds

@Inject
class StopPresenter(
  private val client: RerouteClient,
  private val dataSource: DataSource,
  private val mapInteractionsManager: MapInteractionsManager,
  @Assisted private val navigator: Navigator,
  @Assisted private val args: Stop,
) : Presenter<StopViewModel, StopViewEvent> {
  @Composable
  override fun produceModel(events: Flow<StopViewEvent>): StopViewModel {
    val scope = rememberCoroutineScope()
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

    val realtimeData by produceState<RealtimeMessage?>(initialValue = null, args) {
      whenWindowFocused {
        while (true) {
          value = client.nextTripsSingle(args.code)
          delay(30.seconds)
        }
      }
    }

    val routeSections = remember(routesAtStop) {
      (routesAtStop ?: emptyList()).map {
        RouteSection(
          it.gtfsId,
          it.identifier,
          it.destinations[it.directionId],
          it.directionId,
          null,
        )
      }.sortedWith(compareBy<RouteSection> { it.identifier.length }.thenBy(naturalOrder()) { it.identifier })
    }

    val stop = stopList!!.firstOrNull() ?: return StopViewModel.NotFound

    LaunchedEffect(stop) {
      mapInteractionsManager.goTo(stop)
    }

    DisposableEffect(stop) {
      scope.launch {
        val ids = dataSource.getRouteVariantsAtStop(stop.code).first().map { it.routeVariantId }
        mapInteractionsManager.showRouteVariant(*ids.toTypedArray())
      }

      onDispose {
        mapInteractionsManager.clearRoutes()
      }
    }

    return StopViewModel.Loaded(stop, realtimeData?.let { zipRealtimeData(it, routeSections) } ?: routeSections)
  }

  private fun zipRealtimeData(realtime: RealtimeMessage, routeSections: List<RouteSection>): List<RouteSection> {
    val incoming = realtime.routes.associateBy { "${it.number}-${it.directionId}" }

    return routeSections.map { section ->
      val nextTrips = incoming["${section.identifier}-${section.directionId}"]

      section.copy(nextTrips = nextTrips?.trips)
    }
  }

  // TODO: Replace with fun interface
  @Inject
  class Factory(val create: (Navigator, Stop) -> StopPresenter)
}
