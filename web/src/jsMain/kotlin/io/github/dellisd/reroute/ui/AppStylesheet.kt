package io.github.dellisd.reroute.ui

import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width

object AppStylesheet : StyleSheet() {
    init {
        "html, body, #root" style {
            width(100.percent)
            height(100.percent)
            margin(0.px)
        }
    }

    val map by style {
        width(100.percent)
        height(100.percent)
    }
}
