package ca.derekellis.reroute.data

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import ca.derekellis.reroute.db.DatabaseHelper
import ca.derekellis.reroute.db.Metadata
import ca.derekellis.reroute.di.AppScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.browser.window
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Inject

@AppScope
@Inject
class ResourceLoader(private val withDatabase: DatabaseHelper) {
    private val client = HttpClient(Js) {
        install(ContentNegotiation) {
            json()
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
        val data = client.get("${window.location.origin}/reroute/data.json").body<Data>()
        withDatabase { database ->
            val metadata = database.metadataQueries.get().awaitAsOneOrNull()

            if (metadata == null) {
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
                    val newMetadata = Metadata(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
                    database.metadataQueries.insert(newMetadata)
                }
            }
        }
    }
}
