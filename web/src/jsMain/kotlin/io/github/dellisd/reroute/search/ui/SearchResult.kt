package io.github.dellisd.reroute.search.ui

import androidx.compose.runtime.Composable
import io.github.dellisd.reroute.data.Stop
import io.github.dellisd.reroute.ui.AppStylesheet
import io.github.dellisd.reroute.ui.AppStylesheet.hover
import io.github.dellisd.reroute.ui.AppStylesheet.style
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.pc
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun SearchResult(stop: Stop, onClick: (Stop) -> Unit) {
    Div({
        classes(AppStylesheet.searchResult)
        onClick { onClick(stop) }
    }) {
        Div({ style { fontSize(20.px) } }) { Text(stop.name) }
        Div({ style { fontSize(16.px) } }) { Text(stop.code) }
    }
}
