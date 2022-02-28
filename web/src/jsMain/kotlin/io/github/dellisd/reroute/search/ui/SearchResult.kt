package io.github.dellisd.reroute.search.ui

import androidx.compose.runtime.Composable
import io.github.dellisd.reroute.data.Stop
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun SearchResult(stop: Stop, onClick: (Stop) -> Unit) {
    Div({
        classes(SearchStyleSheet.searchResult)
        onClick { onClick(stop) }
    }) {
        Div({ style { fontSize(20.px) } }) { Text(stop.name) }
        Div({ style { fontSize(16.px) } }) { Text(stop.code) }
    }
}
