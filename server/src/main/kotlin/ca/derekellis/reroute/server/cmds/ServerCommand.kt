package ca.derekellis.reroute.server.cmds

import ca.derekellis.reroute.server.ServerConfig
import ca.derekellis.reroute.server.data.DataHandler
import ca.derekellis.reroute.server.di.RerouteComponent
import ca.derekellis.reroute.server.di.create
import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.decodeFromString
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.copyTo
import kotlin.io.path.exists
import kotlin.io.path.readText

class ServerCommand : CliktCommand() {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val configPath: Path? by option("--config").path()
    private val dataPath: Path? by option("--data").path(mustExist = true, canBeDir = false)

    override fun run() {
        val filePath = configPath ?: Path("config.yml")
        val config =
            if (filePath.exists()) Yaml.default.decodeFromString(filePath.readText()) else ServerConfig()

        val component = RerouteComponent::class.create(config)
        copyDataFile(component.dataHandler)

        embeddedServer(Netty, port = 8888) {
            install(ContentNegotiation) {
                json()
            }
            install(AutoHeadResponse)

            routing {
                component.dataRoute.route()
            }
        }.start(wait = true)
    }

    private fun copyDataFile(dataHandler: DataHandler) {
        if (dataPath != null) {
            logger.info("Copying data file from $dataPath")

            dataPath?.copyTo(dataHandler.dataFile, overwrite = true)
        }
    }
}
