package io.github.dellisd.reroute.search.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import io.github.dellisd.reroute.search.SearchViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input

/**
 * TODO: Styling
 * TODO: Think about how to best expose input events to parent composable. A flow?
 */
@Composable
fun SearchBar(onQueryUpdate: (String) -> Unit) {
    Input(type = InputType.Search) {
        onInput {
            onQueryUpdate(it.value)
        }
    }
}

@Composable
fun Search(viewModel: SearchViewModel) {
    val scope = rememberCoroutineScope()
    val results by viewModel.results.collectAsState()

    Div({
        style {
            width(350.px)
            height(auto)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
        }
    }) {
        SearchBar(onQueryUpdate = { query ->
            scope.launch { viewModel.search(query) }
        })
        results.forEach {
            SearchResult(it)
        }
    }
}
