package ca.derekellis.reroute.server.config

import java.nio.file.Path

data class LoadedServerConfig(
    override val dataPath: Path
) : ServerConfig
