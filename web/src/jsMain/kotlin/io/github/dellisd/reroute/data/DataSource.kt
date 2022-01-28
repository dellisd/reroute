package io.github.dellisd.reroute.data

import kotlinx.coroutines.flow.Flow

interface DataSource {
    suspend fun getStop(id: String): Stop?

    suspend fun searchStops(query: String): List<Stop>

    suspend fun getStops(): Flow<List<Stop>>
}
