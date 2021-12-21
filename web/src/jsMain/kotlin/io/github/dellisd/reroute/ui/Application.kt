package io.github.dellisd.reroute.ui

import androidx.compose.runtime.Composable
import io.github.dellisd.reroute.search.SearchViewModel
import io.github.dellisd.reroute.search.ui.Search
import me.tatarka.inject.annotations.Inject

typealias Application = @Composable () -> Unit

/**
 * TODO: Don't pass searchViewModel like this
 */
@Inject
@Composable
fun Application(searchViewModel: SearchViewModel) {
    Search(searchViewModel)
}
