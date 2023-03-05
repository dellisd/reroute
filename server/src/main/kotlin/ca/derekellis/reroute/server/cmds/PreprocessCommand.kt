package ca.derekellis.reroute.server.cmds

import ca.derekellis.kgtfs.cache.GtfsCache
import ca.derekellis.kgtfs.io.GtfsReader
import ca.derekellis.reroute.server.data.DataBundler
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.div
import kotlin.io.path.notExists
import kotlin.io.path.outputStream

class PreprocessCommand : CliktCommand() {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val source: Path by argument().path(canBeDir = false)
    private val output: Path by option("--output", "-o").path(canBeFile = false).default(Path("data"))
    private val name: String by option("--name", "-n").default("gtfs")

    @OptIn(ExperimentalSerializationApi::class)
    override fun run() {
        if (output.notExists()) {
            output.createDirectories()
        }

        val cachePath = output / "$name.db"
        cachePath.deleteIfExists()

        logger.info("Reading {} into cache at {}", source, cachePath)
        val cache = GtfsCache.fromReader(cachePath, GtfsReader(source))

        logger.info("Bundling data into {}/{}.json", output, name)
        val bundler = DataBundler()

        val bundle = bundler.assembleDataBundle(cache)
        val bundleFile = output / "$name.json"
        bundleFile.outputStream().use { stream ->
            Json.encodeToStream(bundle, stream)
        }
    }
}
