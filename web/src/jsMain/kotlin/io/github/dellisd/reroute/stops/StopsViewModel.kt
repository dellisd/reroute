package io.github.dellisd.reroute.stops

import io.github.dellisd.reroute.data.DataSource
import io.github.dellisd.reroute.data.Stop
import io.github.dellisd.reroute.di.AppScope
import io.github.dellisd.reroute.map.MapInteractionsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class StopsViewModel(private val dataSource: DataSource, private val mapInteractionsManager: MapInteractionsManager) {
    fun stop(code: String): Flow<Stop?> = flow {
        emitAll(dataSource.getStopByCode(code).map(List<Stop>::firstOrNull))
    }

    suspend fun goTo(stop: Stop) {
        mapInteractionsManager.goTo(stop)
    }
}
