package ca.derekellis.reroute.server.config

import kotlinx.serialization.Serializable
import java.nio.file.Path

@Serializable
data class SerializableServerConfig(override val dataPath: Path? = null) : ServerConfig
