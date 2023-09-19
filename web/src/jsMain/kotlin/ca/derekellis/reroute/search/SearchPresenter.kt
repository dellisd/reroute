package ca.derekellis.reroute.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ca.derekellis.reroute.data.DataSource
import ca.derekellis.reroute.models.Stop
import ca.derekellis.reroute.search.SearchViewEvent.UpdateQuery
import ca.derekellis.reroute.ui.CollectEffect
import ca.derekellis.reroute.ui.Navigator
import ca.derekellis.reroute.ui.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ca.derekellis.reroute.stops.Stop as StopScreen

@Inject
class SearchPresenter(
  private val dataSource: DataSource,
  private val navigator: Navigator,
  private val args: Search,
) : Presenter<SearchViewModel, SearchViewEvent> {
  @Composable
  override fun produceModel(events: Flow<SearchViewEvent>): SearchViewModel {
    var results by remember { mutableStateOf(emptyList<Stop>()) }

    CollectEffect(events) { event ->
      when (event) {
        is UpdateQuery -> launch {
          results = dataSource.searchStops(event.query).first()
        }

        is SearchViewEvent.SelectStop -> launch {
          navigator.goTo(StopScreen(event.stop.code))
        }
      }
    }

    return SearchViewModel("", results)
  }
}
