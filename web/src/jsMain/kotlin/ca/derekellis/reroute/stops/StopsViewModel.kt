package ca.derekellis.reroute.stops

import ca.derekellis.reroute.data.DataSource
import ca.derekellis.reroute.data.Stop
import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.map.MapInteractionsManager
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
