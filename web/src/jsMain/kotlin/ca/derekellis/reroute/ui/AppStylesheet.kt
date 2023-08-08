package ca.derekellis.reroute.ui

import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.media
import org.jetbrains.compose.web.css.mediaMaxWidth
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.width

object AppStylesheet : StyleSheet() {
  init {
    "html, body, #root" style {
      width(100.percent)
      height(100.percent)
      margin(0.px)
      fontFamily("Roboto", "sans-serif")
    }
  }

  val map by style {
    width(100.percent)
    height(100.percent)
  }

  val overlayContainer by style {
    position(Position.Absolute)
    top(0.px)
    left(0.px)
    width(512.px)
    media(SmallMediaQuery) {
      self style {
        width(100.percent)
      }
    }
  }

  val SmallMediaQuery = mediaMaxWidth(600.px)
}
