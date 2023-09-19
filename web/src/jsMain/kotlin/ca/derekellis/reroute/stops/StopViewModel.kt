package ca.derekellis.reroute.stops

import ca.derekellis.reroute.models.Stop

sealed interface StopViewModel {
  object Loading : StopViewModel

  data class Loaded(
    val stop: Stop,
    val groupedRoutes: List<RouteSection>,
  ) : StopViewModel {
    data class RouteSection(val gtfsId: String, val identifier: String, val name: String, val directionId: Int)
  }

  object NotFound : StopViewModel
}
