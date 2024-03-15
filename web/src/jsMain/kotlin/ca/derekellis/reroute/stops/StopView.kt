package ca.derekellis.reroute.stops

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ca.derekellis.reroute.realtime.NextTrip
import ca.derekellis.reroute.ui.InfoPanel
import ca.derekellis.reroute.ui.View
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Date
import kotlin.time.Duration.Companion.milliseconds

class StopView : View<StopViewModel, StopViewEvent> {
  @Composable
  override fun Content(model: StopViewModel, emit: (StopViewEvent) -> Unit) {
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
      H2 { Text(model.stop.code) }
      Div { Text(model.stop.id) }
      Hr()
      model.groupedRoutes.forEach { routes -> RouteInfo(routes) }
    }
    StopViewModel.Loading -> Text("Loading...")
    StopViewModel.NotFound -> Text("Not Found!")
  }
}

@Composable
private fun RouteInfo(route: RouteSection) {
  Div {
    H3 {
      Text("${route.identifier} ${route.name}")
    }
    if (route.nextTrips == null) {
      Text("No upcoming trips")
    } else {
      route.nextTrips.forEach {
        UpcomingTrip(it)
      }
    }
  }
}

private fun currentTimeOfDay(): Int {
  val date = Date()
  return date.getHours() * 3_600_000 + date.getMinutes() * 60_000 + date.getSeconds() * 1000
}

@Composable
private fun UpcomingTrip(nextTrip: NextTrip) {
  Div(attrs = {
    style {
      display(DisplayStyle.Flex)
      flexDirection(FlexDirection.Row)
    }
  }) {
    val time = remember { (nextTrip.adjustedScheduleTime.toMillisecondOfDay() - currentTimeOfDay()).milliseconds }

    Span { Text("${time.inWholeMinutes} min") }
    Span(attrs = {
      style {
        marginLeft(4.px)
        marginRight(4.px)
      }
    }) { Text("→") }
    Span { Text(nextTrip.destination) }
  }
}