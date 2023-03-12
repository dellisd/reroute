package ca.derekellis.reroute.server.di

import ca.derekellis.reroute.server.RerouteServer
import ca.derekellis.reroute.server.config.LoadedServerConfig
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@RerouteScope
abstract class RerouteComponent(
  private val config: LoadedServerConfig,
) {
  abstract val rerouteServer: RerouteServer

  @Provides
  fun config(): LoadedServerConfig = config
}
