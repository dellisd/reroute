package ca.derekellis.reroute

import ca.derekellis.reroute.stops.Stop
import ca.derekellis.reroute.stops.StopPresenter
import ca.derekellis.reroute.ui.Presenter
import ca.derekellis.reroute.ui.Screen
import me.tatarka.inject.annotations.Inject

@Inject
class PresenterFactory(
    private val stopPresenterFactory: (Stop) -> StopPresenter
) {
    fun createPresenter(screen: Screen): Presenter<*, *> = when (screen) {
        is Stop -> stopPresenterFactory(screen)
        else -> throw IllegalArgumentException("Unsupported screen $screen")
    }
}
