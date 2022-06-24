package ca.derekellis.reroute.data

import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.models.Stop
import kotlinx.coroutines.flow.Flow

interface DataSource {
    suspend fun getStop(id: String): Stop?

    suspend fun getStopByCode(code: String): Flow<List<Stop>>

    suspend fun searchStops(query: String): List<Stop>

    suspend fun getStops(): Flow<List<Stop>>

    suspend fun getRoutesAtStop(code: String): Flow<List<Route>>
}
