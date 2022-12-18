package ca.derekellis.reroute.search

import ca.derekellis.reroute.data.DataSource
import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.map.MapInteractionsManager
import ca.derekellis.reroute.models.Stop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject

@AppScope
@Inject
class SearchViewModel(private val dataSource: DataSource, private val mapInteractionsManager: MapInteractionsManager) {
    private val _results = MutableStateFlow(emptyList<Stop>())
    val results = _results.asStateFlow()

    suspend fun search(query: String) {
        val result = dataSource.searchStops(query)
        _results.value = result.first()
    }

    suspend fun selectStop(stop: Stop) {
        mapInteractionsManager.goTo(stop)
    }
}
