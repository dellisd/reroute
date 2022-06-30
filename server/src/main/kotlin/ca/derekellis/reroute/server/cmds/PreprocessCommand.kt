package ca.derekellis.reroute.server.cmds

import ca.derekellis.reroute.server.ServerConfig
import ca.derekellis.reroute.server.di.RerouteComponent
import ca.derekellis.reroute.server.di.create
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import kotlinx.coroutines.runBlocking
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.copyTo

class PreprocessCommand : CliktCommand() {
    private val source: String by argument()
    private val output: Path by option("--output", "-o").path().default(Path("./data.json"))

    override fun run() {
        val component = RerouteComponent::class.create(ServerConfig(source))
        val handler = component.dataHandler

        val path = runBlocking { handler.getDataFile() }
        path.copyTo(output, overwrite = true)
    }
}
