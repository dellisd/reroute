package ca.derekellis.reroute.server.cmds

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

class MainCommand : CliktCommand() {
    init {
        subcommands(ServerCommand(), PreprocessCommand())
    }

    override fun run() {}
}
