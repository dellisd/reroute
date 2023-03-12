package ca.derekellis.reroute.stops

import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.models.Stop

sealed interface StopViewModel {
  object Loading : StopViewModel

  data class Loaded(
    val stop: Stop,
    val groupedRoutes: List<List<Route>>,
  ) : StopViewModel

  object NotFound : StopViewModel
}
