package io.github.dellisd.reroute.data

import io.github.dellisd.reroute.db.DatabaseHelper
import io.github.dellisd.reroute.di.AppScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Inject

@AppScope
@Inject
class ResourceLoader(private val withDatabase: DatabaseHelper) {
    private val client = HttpClient(Js) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    @Serializable
    private data class Stop(
        @SerialName("stop_id") val id: String,
        @SerialName("stop_code") val code: String? = null,
        @SerialName("stop_name") val name: String,
        @SerialName("stop_lat") val lat: Double,
        @SerialName("stop_lon") val lon: Double,
        @SerialName("location_type") val locationType: Long
    )

    @Serializable
    private data class Data(val stops: List<Stop>)

    suspend fun loadStopsToDatabase() {
        val data = client.get<Data>("http://localhost:8080/data.json")
        withDatabase { database ->
            database.stopsQueries.transaction {
                data.stops.forEach {
                    database.stopsQueries.insert(
                        it.id,
                        it.code ?: "",
                        it.name,
                        null,
                        it.lat,
                        it.lon,
                        null,
                        null,
                        it.locationType
                    )
                }
            }

            console.log(database.stopSearchQueries.getById("AA010").executeAsOne())
        }
    }
}
