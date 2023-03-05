package ca.derekellis.reroute.server.di

import ca.derekellis.kgtfs.cache.GtfsCache
import ca.derekellis.reroute.server.config.LoadedServerConfig
import ca.derekellis.reroute.server.routes.DataRoute
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import kotlin.io.path.div

@Component
@RerouteScope
abstract class RerouteComponent(private val config: LoadedServerConfig) {
    abstract val dataRoute: DataRoute

    @Provides
    fun config(): LoadedServerConfig = config

    @Provides
    fun provideGtfsCache(config: LoadedServerConfig): GtfsCache = GtfsCache.open(config.dataPath / "gtfs.db")
}
