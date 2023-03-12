package ca.derekellis.reroute.data

import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.models.TransitDataBundle
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.parse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.serialization.kotlinx.json.json
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class RerouteClient {
  private val client = HttpClient(Js) {
    install(ContentNegotiation) {
      json()
    }
  }

  suspend fun getDataBundleDate(): DateTime {
    val headers = client.head("/api/data/").headers
    val formatter = DateFormat("EEE, dd MMM yyyy HH:mm:ss z")

    return headers["Last-Modified"]!!.let { formatter.parse(it) }.utc
  }

  suspend fun getDataBundle(): TransitDataBundle = client.get("/api/data/").body()
}
