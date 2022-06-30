package ca.derekellis.reroute.server.routes

import ca.derekellis.reroute.server.data.DataHandler
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

@Inject
@RerouteScope
class DataRoute(private val dataHandler: DataHandler) : RoutingModule {
    context(Routing)
    override fun route() = route("data") {
        get("/") {
            val path = dataHandler.getDataFile()
            val attrs = withContext(Dispatchers.IO) {
                Files.readAttributes(
                    dataHandler.getDataFile(),
                    BasicFileAttributes::class.java
                )
            }

            call.response.lastModified(
                ZonedDateTime.ofInstant(
                    attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault()
                )
            )

            call.respondFile(path.toFile())
        }
    }
}
