package ca.derekellis.reroute.server.cmds

import ca.derekellis.reroute.server.config.LoadedServerConfig
import ca.derekellis.reroute.server.config.SerializableServerConfig
import ca.derekellis.reroute.server.di.RerouteComponent
import ca.derekellis.reroute.server.di.create
import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText

class ServerCommand : CliktCommand() {
    private val configPath: Path by option("--config").path().default(Path("config.yml"))

    override fun run() {
        val yamlConfig = configPath.takeIf { it.exists() }?.let { path ->
            Yaml.default.decodeFromString<SerializableServerConfig>(path.readText())
        }

        val config = LoadedServerConfig(
            yamlConfig?.dataPath ?: Path("data")
        )

        val component = RerouteComponent::class.create(config)

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
}
