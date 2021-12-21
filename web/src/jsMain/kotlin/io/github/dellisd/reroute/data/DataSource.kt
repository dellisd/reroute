package io.github.dellisd.reroute.data

interface DataSource {
    suspend fun getStop(id: String): Stop?

    suspend fun searchStops(query: String): List<Stop>
}
