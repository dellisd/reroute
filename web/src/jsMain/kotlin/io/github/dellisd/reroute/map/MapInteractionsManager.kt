package io.github.dellisd.reroute.map

import io.github.dellisd.reroute.data.Stop
import io.github.dellisd.reroute.di.AppScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.tatarka.inject.annotations.Inject

@AppScope
@Inject
class MapInteractionsManager {
    private val _targetStop = MutableSharedFlow<Stop>()
    val targetStop = _targetStop.asSharedFlow()

    suspend fun goTo(stop: Stop) {
        _targetStop.emit(stop)
    }
}
