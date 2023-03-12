package ca.derekellis.reroute.search

import ca.derekellis.reroute.models.Stop

// @AppScope
// @Inject
// class SearchViewModel(private val dataSource: DataSource, private val mapInteractionsManager: MapInteractionsManager) {
//    private val _results = MutableStateFlow(emptyList<Stop>())
//    val results = _results.asStateFlow()
//
//    suspend fun search(query: String) {
//        val result = dataSource.searchStops(query)
//        _results.value = result.first()
//    }
//
//    suspend fun selectStop(stop: Stop) {
//        mapInteractionsManager.goTo(stop)
//    }
// }

data class SearchViewModel(val queryText: String, val results: List<Stop>)
