package ca.derekellis.reroute.data

import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.models.Stop
import kotlinx.coroutines.flow.Flow

interface DataSource {
    fun getStop(id: String): Flow<Stop?>

    fun getStopByCode(code: String): Flow<List<Stop>>

    fun searchStops(query: String): Flow<List<Stop>>

    fun getStops(): Flow<List<Stop>>

    fun getRoutesAtStop(code: String): Flow<List<Route>>
}
