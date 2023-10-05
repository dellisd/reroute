package ca.derekellis.reroute.stops

import ca.derekellis.reroute.models.Stop

sealed interface StopViewModel {
  object Loading : StopViewModel

  data class Loaded(
    val stop: Stop,
    val groupedRoutes: List<RouteSection>,
  ) : StopViewModel

  object NotFound : StopViewModel
}
