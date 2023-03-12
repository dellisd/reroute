package ca.derekellis.reroute.server

import ca.derekellis.reroute.server.cmds.PreprocessCommand
import ca.derekellis.reroute.server.cmds.ServerCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
  NoOpCliktCommand(name = "reroute")
    .subcommands(
      PreprocessCommand(),
      ServerCommand(),
    )
    .main(args)
}
