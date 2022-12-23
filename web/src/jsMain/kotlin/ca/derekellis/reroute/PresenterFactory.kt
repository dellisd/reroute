package ca.derekellis.reroute

import ca.derekellis.reroute.home.Home
import ca.derekellis.reroute.home.HomePresenter
import ca.derekellis.reroute.map.Map
import ca.derekellis.reroute.map.MapPresenter
import ca.derekellis.reroute.search.Search
import ca.derekellis.reroute.search.SearchPresenter
import ca.derekellis.reroute.stops.Stop
import ca.derekellis.reroute.stops.StopPresenter
import ca.derekellis.reroute.ui.Presenter
import ca.derekellis.reroute.ui.Screen
import me.tatarka.inject.annotations.Inject

@Inject
class PresenterFactory(
    private val stopPresenterFactory: (Stop) -> StopPresenter,
    private val homePresenterFactory: (Home) -> HomePresenter,
    private val mapPresenterFactory: (Map) -> MapPresenter,
    private val searchPresenterFactory: (Search) -> SearchPresenter,
) {
    fun createPresenter(screen: Screen): Presenter<*, *> = when (screen) {
        is Home -> homePresenterFactory(screen)
        is Stop -> stopPresenterFactory(screen)
        is Map -> mapPresenterFactory(screen)
        is Search -> searchPresenterFactory(screen)
        else -> throw IllegalArgumentException("Unsupported screen $screen")
    }
}
