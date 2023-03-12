package ca.derekellis.reroute.map

import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.models.Stop
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.tatarka.inject.annotations.Inject

@AppScope
@Inject
class MapInteractionsManager {
  private val _targetStop = MutableSharedFlow<Stop?>()
  val targetStop = _targetStop.asSharedFlow()

  suspend fun goTo(stop: Stop?) {
    _targetStop.emit(stop)
  }
}
