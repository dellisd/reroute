package ca.derekellis.reroute.server.routes

import ca.derekellis.reroute.server.config.LoadedServerConfig
import ca.derekellis.reroute.server.di.RerouteScope
import io.ktor.server.application.call
import io.ktor.server.response.lastModified
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.io.path.div

@Inject
@RerouteScope
class DataRoute(private val config: LoadedServerConfig) : RoutingModule {
  private val filePath = config.dataPath / "gtfs.json"

  context(Routing)
  override fun route() = route("data") {
    get("/") {
      val attrs = withContext(Dispatchers.IO) {
        Files.readAttributes(
          filePath,
          BasicFileAttributes::class.java,
        )
      }

      call.response.lastModified(
        ZonedDateTime.ofInstant(
          attrs.lastModifiedTime().toInstant(),
          ZoneId.systemDefault(),
        ),
      )

      call.respondFile(filePath.toFile())
    }
  }
}
