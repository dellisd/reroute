package ca.derekellis.reroute.stops

import androidx.compose.runtime.Composable
import ca.derekellis.reroute.models.Route
import ca.derekellis.reroute.ui.InfoPanel
import ca.derekellis.reroute.ui.View
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Text

class StopView : View<StopViewModel, StopViewEvent> {
  @Composable
  override fun Content(model: StopViewModel?, emit: (StopViewEvent) -> Unit) {
    model ?: return

    Style(StopsStyleSheet)
    InfoPanel(onClose = { emit(Close) }) {
      StopContent(model)
    }
  }
}

object StopsStyleSheet : StyleSheet()

@Composable
private fun StopContent(model: StopViewModel) {
  when (model) {
    is StopViewModel.Loaded -> {
      H1 { Text(model.stop.name) }
      H2 { Text(model.stop.code ?: "") }
      Div { Text(model.stop.id) }
      Hr()
      model.groupedRoutes.forEach { routes -> RouteInfo(routes) }
    }
    StopViewModel.Loading -> Text("Loading...")
    StopViewModel.NotFound -> Text("Not Found!")
  }
}

@Composable
private fun RouteInfo(variants: List<Route>) {
  val top = variants.first()
  val rest = variants.drop(1)

  Div {
    H3 {
      Text("${top.name} ${top.headsign}")
    }
    rest.forEach { route ->
      Div {
        Text(route.headsign)
      }
    }
  }
}
