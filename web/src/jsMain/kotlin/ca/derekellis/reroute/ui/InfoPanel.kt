package ca.derekellis.reroute.ui

import androidx.compose.runtime.Composable
import ca.derekellis.reroute.ui.AppStylesheet.SmallMediaQuery
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.background
import org.jetbrains.compose.web.css.boxSizing
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.media
import org.jetbrains.compose.web.css.overflowY
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.right
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

object InfoPanelStylesheet : StyleSheet() {
  val infoPanelContainer by style {
    width(32.em)
    height(100.percent)
    background("#FFFFFF")
    position(Position.Fixed)
    right(0.px)
    top(0.px)
    padding(0.5.em)
    overflowY("auto")
    boxSizing("border-box")

    media(SmallMediaQuery) {
      self style {
        width(100.percent)
      }
    }
  }
}

@Composable
fun InfoPanel(onClose: () -> Unit, content: @Composable () -> Unit) {
  Style(InfoPanelStylesheet)
  Div(attrs = {
    classes(InfoPanelStylesheet.infoPanelContainer)
  }) {
    Button(attrs = {
      onClick { onClose() }
    }) {
      Text("Close")
    }
    content()
  }
}
