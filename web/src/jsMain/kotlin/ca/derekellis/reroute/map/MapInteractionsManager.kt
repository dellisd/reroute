package ca.derekellis.reroute.map

import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.models.Stop
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject

@AppScope
@Inject
class MapInteractionsManager {
  private val _targetStop = MutableSharedFlow<Stop?>()
  val targetStop = _targetStop.asSharedFlow()

  private val _routeVariants = MutableStateFlow<Set<String>>(emptySet())
  val routeVariants = _routeVariants.asStateFlow()

  suspend fun goTo(stop: Stop?) {
    _targetStop.emit(stop)
  }

  fun showRouteVariant(vararg variantIds: String) {
    _routeVariants.value += variantIds
  }

  fun clearRoutes() {
    _routeVariants.value = emptySet()
  }
}
