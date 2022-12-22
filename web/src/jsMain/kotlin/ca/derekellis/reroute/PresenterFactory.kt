package ca.derekellis.reroute

import ca.derekellis.reroute.home.Home
import ca.derekellis.reroute.home.HomePresenter
import ca.derekellis.reroute.stops.Stop
import ca.derekellis.reroute.stops.StopPresenter
import ca.derekellis.reroute.ui.Presenter
import ca.derekellis.reroute.ui.Screen
import me.tatarka.inject.annotations.Inject

@Inject
class PresenterFactory(
    private val stopPresenterFactory: (Stop) -> StopPresenter,
    private val homePresenterFactory: (Home) -> HomePresenter,
) {
    fun createPresenter(screen: Screen): Presenter<*, *> = when (screen) {
        is Home -> homePresenterFactory(screen)
        is Stop -> stopPresenterFactory(screen)
        else -> throw IllegalArgumentException("Unsupported screen $screen")
    }
}
