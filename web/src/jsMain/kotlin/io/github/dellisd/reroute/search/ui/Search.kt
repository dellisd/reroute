@file:Suppress("FunctionName")

package io.github.dellisd.reroute.search.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import io.github.dellisd.reroute.search.SearchViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.boxSizing
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.hsl
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.overflow
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input

object SearchStyleSheet : StyleSheet() {
    val searchContainer by style {
        margin(16.px)
        width(350.px)
        height(auto)
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
    }

    val searchInput by style {
        padding(8.px)
        border(width = 1.px, style = LineStyle.Solid, color = hsl(0, 0, 93))
        borderRadius(8.px)
        property("box-shadow", "0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24)")
    }

    val resultsContainer by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        width(100.percent)
        backgroundColor(Color.white)
        borderRadius(8.px)
        marginTop(8.px)
        overflow("hidden")
        property("user-select", "none")
    }

    val searchResult by style {
        display(DisplayStyle.Flex)
        width(100.percent)
        height(72.px)
        flexDirection(FlexDirection.Column)
        justifyContent(JustifyContent.Center)
        boxSizing("border-box")
        padding(9.px)

        hover(self) style {
            backgroundColor(hsl(0, 0, 93))
            cursor("pointer")
        }
    }
}

/**
 * TODO: Styling
 * TODO: Think about how to best expose input events to parent composable. A flow?
 */
@Composable
fun SearchBar(onQueryUpdate: (String) -> Unit) {
    Input(type = InputType.Search, attrs = {
        classes(SearchStyleSheet.searchInput)
        onInput {
            onQueryUpdate(it.value)
        }
    })
}

@Composable
fun Search(viewModel: SearchViewModel) {
    val scope = rememberCoroutineScope()
    val results by viewModel.results.collectAsState()

    Style(SearchStyleSheet)

    Div(attrs = {
        classes(SearchStyleSheet.searchContainer)
    }) {
        SearchBar(onQueryUpdate = { query ->
            scope.launch { viewModel.search(query) }
        })
        Div(attrs = {
            classes(SearchStyleSheet.resultsContainer)
        }) {
            results.forEach {
                SearchResult(it, onClick = {
                    scope.launch {
                        viewModel.selectStop(it)
                    }
                })
            }
        }
    }
}
