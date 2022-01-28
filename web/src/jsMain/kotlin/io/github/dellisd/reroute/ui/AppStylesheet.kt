package io.github.dellisd.reroute.ui

import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.hsl
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.pc
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
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

    val searchResult by style {
        display(DisplayStyle.Flex)
        width(100.pc)
        height(72.px)
        flexDirection(FlexDirection.Column)

        hover(self) style {
            backgroundColor(hsl(0, 0, 93))
            cursor("pointer")
        }
    }

    val overlayContainer by style {
        position(Position.Absolute)
        top(0.px)
        left(0.px)
    }
}
