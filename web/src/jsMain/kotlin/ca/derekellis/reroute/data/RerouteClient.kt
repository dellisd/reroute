package ca.derekellis.reroute.data

import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.models.TransitDataBundle
import ca.derekellis.reroute.realtime.RealtimeMessage
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.parse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.head
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class RerouteClient(private val client: HttpClient) {

  suspend fun getDataBundleDate(): DateTime {
    val headers = client.head("/api/data/").headers
    val formatter = DateFormat("EEE, dd MMM yyyy HH:mm:ss z")

    return headers["Last-Modified"]!!.let { formatter.parse(it) }.utc
  }

  suspend fun getDataBundle(): TransitDataBundle = client.get("/api/data/").body()

  @Deprecated("Websocket support is being dropped")
  fun nextTrips(code: String): Flow<RealtimeMessage> = flow {
    client.webSocket("/api/realtime/$code") {
      while (true) {
        emit(receiveDeserialized<RealtimeMessage>())
      }
    }
  }

  suspend fun nextTripsSingle(code: String): RealtimeMessage = client.get("/api/realtime/$code").body()
}
