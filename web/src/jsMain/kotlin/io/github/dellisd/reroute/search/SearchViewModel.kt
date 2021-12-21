package io.github.dellisd.reroute.search

import io.github.dellisd.reroute.data.DataSource
import io.github.dellisd.reroute.data.Stop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject

@Inject
class SearchViewModel(private val dataSource: DataSource) {
    private val _results = MutableStateFlow(emptyList<Stop>())
    val results = _results.asStateFlow()

    suspend fun search(query: String) {
        _results.value = dataSource.searchStops(query)
    }
}
