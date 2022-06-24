package ca.derekellis.reroute.server.di

import ca.derekellis.reroute.server.routes.DataRoute
import me.tatarka.inject.annotations.Component

@Component
@ServerScope
abstract class ServerComponent {
    abstract val dataRoute: DataRoute
}
