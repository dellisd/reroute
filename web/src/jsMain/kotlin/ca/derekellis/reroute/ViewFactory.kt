package ca.derekellis.reroute

import ca.derekellis.reroute.home.Home
import ca.derekellis.reroute.home.HomeView
import ca.derekellis.reroute.stops.Stop
import ca.derekellis.reroute.stops.StopView
import ca.derekellis.reroute.ui.Screen
import ca.derekellis.reroute.ui.View
import me.tatarka.inject.annotations.Inject

@Inject
class ViewFactory {
    fun createView(screen: Screen): View<*, *> {
        return when (screen) {
            is Home -> HomeView()
            is Stop -> StopView()
            else -> throw IllegalArgumentException("Unsupported screen $screen")
        }
    }
}
