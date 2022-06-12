package ca.derekellis.reroute.data

import kotlinx.coroutines.flow.Flow

interface DataSource {
    suspend fun getStop(id: String): Stop?

    suspend fun getStopByCode(code: String): Flow<List<Stop>>

    suspend fun searchStops(query: String): List<Stop>

    suspend fun getStops(): Flow<List<Stop>>
}