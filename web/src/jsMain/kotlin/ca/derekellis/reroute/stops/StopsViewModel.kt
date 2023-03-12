package ca.derekellis.reroute.stops

import ca.derekellis.reroute.data.DataSource
import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.map.MapInteractionsManager
import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.models.Stop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

typealias StopModel = Pair<Stop, List<List<Route>>>

@Inject
@AppScope
class StopsViewModel(private val dataSource: DataSource, private val mapInteractionsManager: MapInteractionsManager) {
  fun stop(code: String): Flow<StopModel?> = flow {
    val groupedRoutes = dataSource.getRoutesAtStop(code)
      .map { list ->
        list.groupBy { "${it.name}-${it.directionId}" }
          .values
          .sortedBy { it.first().name.filter(Char::isDigit).toInt() }
      }

    val combined = combine(
      dataSource.getStopByCode(code).map(List<Stop>::firstOrNull),
      groupedRoutes,
    ) { stop, routes ->
      stop?.let { it to routes }
    }

    emitAll(combined)
  }

  suspend fun goTo(stop: Stop?) {
    mapInteractionsManager.goTo(stop)
  }
}
