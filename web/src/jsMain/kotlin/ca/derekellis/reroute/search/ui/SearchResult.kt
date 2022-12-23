package ca.derekellis.reroute.search.ui

import androidx.compose.runtime.Composable
import ca.derekellis.reroute.models.Stop
import ca.derekellis.reroute.search.SearchStyleSheet
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun SearchResult(stop: Stop, onClick: (Stop) -> Unit) {
    Div({
        classes(SearchStyleSheet.searchResult)
        onMouseDown { onClick(stop) }
    }) {
        Div({ style { fontSize(20.px) } }) { Text(stop.name) }
        stop.code?.let { code -> Div({ style { fontSize(16.px) } }) { Text(code) } }

    }
}
