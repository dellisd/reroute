package ca.derekellis.reroute

import ca.derekellis.reroute.home.Home
import ca.derekellis.reroute.home.HomePresenter
import ca.derekellis.reroute.map.Map
import ca.derekellis.reroute.map.MapPresenter
import ca.derekellis.reroute.search.Search
import ca.derekellis.reroute.search.SearchPresenter
import ca.derekellis.reroute.stops.Stop
import ca.derekellis.reroute.stops.StopPresenter
import ca.derekellis.reroute.ui.Navigator
import ca.derekellis.reroute.ui.Presenter
import ca.derekellis.reroute.ui.Screen
import me.tatarka.inject.annotations.Inject

@Inject
class PresenterFactory(
  private val stopPresenterFactory: StopPresenter.Factory,
  private val homePresenterFactory: HomePresenter.Factory,
  private val mapPresenterFactory: MapPresenter.Factory,
  private val searchPresenterFactory: SearchPresenter.Factory,
) {
  fun createPresenter(navigator: Navigator, screen: Screen): Presenter<*, *> = when (screen) {
    is Home -> homePresenterFactory.create(screen)
    is Stop -> stopPresenterFactory.create(navigator, screen)
    is Map -> mapPresenterFactory.create(navigator, screen)
    is Search -> searchPresenterFactory.create(navigator, screen)
    else -> throw IllegalArgumentException("Unsupported screen $screen")
  }
}
