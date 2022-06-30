package ca.derekellis.reroute.server.di

import ca.derekellis.reroute.server.ServerConfig
import ca.derekellis.reroute.server.data.DataHandler
import ca.derekellis.reroute.server.routes.DataRoute
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@RerouteScope
abstract class RerouteComponent(private val config: ServerConfig) {
    abstract val dataRoute: DataRoute

    abstract val dataHandler: DataHandler

    @Provides
    fun config(): ServerConfig = config
}
