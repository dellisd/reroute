package ca.derekellis.reroute.server.data

import ca.derekellis.reroute.server.RoutingModule
import ca.derekellis.reroute.server.config.LoadedServerConfig
import ca.derekellis.reroute.server.di.RerouteScope
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.lastModified
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.io.path.div
import kotlin.io.path.notExists
import kotlin.io.path.pathString

@Inject
@RerouteScope
class DataRoute(private val config: LoadedServerConfig) : RoutingModule {
  private val logger = LoggerFactory.getLogger(javaClass)

  // TODO: Parameterize these data filenames
  private val filePath = config.dataPath / "gtfs.json"
  private val geojsonPath = config.dataPath / "gtfs.geojson"

  context(Routing)
  override fun route() = route("data") {
    get("/") {
      if (filePath.notExists()) {
        logger.warn("Data file not found! Searched at {}", filePath.pathString)
        call.respond(HttpStatusCode.NotFound)
      } else {
        lastModifiedOf(filePath)
        call.respondFile(filePath.toFile())
      }
    }

    get("/geojson") {
      if (filePath.notExists()) {
        logger.warn("GeoJSON file not found! Searched at {}", filePath.pathString)
        call.respond(HttpStatusCode.NotFound)
      } else {
        lastModifiedOf(geojsonPath)
        call.respondFile(geojsonPath.toFile())
      }
    }
  }

  private suspend fun PipelineContext<Unit, ApplicationCall>.lastModifiedOf(path: Path) {
    val attrs = withContext(Dispatchers.IO) {
      Files.readAttributes(
        path,
        BasicFileAttributes::class.java,
      )
    }

    call.response.lastModified(
      ZonedDateTime.ofInstant(
        attrs.lastModifiedTime().toInstant(),
        ZoneId.systemDefault(),
      ),
    )
  }
}
