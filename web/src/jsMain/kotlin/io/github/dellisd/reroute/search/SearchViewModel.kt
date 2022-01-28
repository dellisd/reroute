package io.github.dellisd.reroute.search

import io.github.dellisd.reroute.data.DataSource
import io.github.dellisd.reroute.data.Stop
import io.github.dellisd.reroute.di.AppScope
import io.github.dellisd.reroute.map.MapInteractionsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject

@AppScope
@Inject
class SearchViewModel(private val dataSource: DataSource, private val mapInteractionsManager: MapInteractionsManager) {
    private val _results = MutableStateFlow(emptyList<Stop>())
    val results = _results.asStateFlow()

    suspend fun search(query: String) {
        val result = dataSource.searchStops(query)
        _results.value = result
    }

    suspend fun selectStop(stop: Stop) {
        mapInteractionsManager.goTo(stop)
    }
}
