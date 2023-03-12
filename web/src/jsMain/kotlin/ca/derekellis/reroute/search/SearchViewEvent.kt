package ca.derekellis.reroute.search

import ca.derekellis.reroute.models.Stop

sealed interface SearchViewEvent {
  data class UpdateQuery(val query: String) : SearchViewEvent

  data class SelectStop(val stop: Stop) : SearchViewEvent
}
