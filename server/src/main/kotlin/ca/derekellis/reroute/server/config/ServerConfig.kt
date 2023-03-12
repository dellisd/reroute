package ca.derekellis.reroute.server.config

import java.nio.file.Path

interface ServerConfig {
  val dataPath: Path?
}
