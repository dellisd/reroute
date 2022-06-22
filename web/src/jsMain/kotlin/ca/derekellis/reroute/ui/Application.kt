package ca.derekellis.reroute.ui

import androidx.compose.runtime.Composable
import ca.derekellis.reroute.search.SearchViewModel
import ca.derekellis.reroute.search.ui.Search
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.web.dom.Div

typealias Application = @Composable () -> Unit

/**
 * TODO: Don't pass searchViewModel like this
 */
@Inject
@Composable
fun Application(searchViewModel: SearchViewModel) {
    Div(attrs = {
        classes(AppStylesheet.overlayContainer)
    }) {
        Search(searchViewModel)
    }
}
