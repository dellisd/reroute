package ca.derekellis.reroute.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import ca.derekellis.mapbox.LngLat
import ca.derekellis.mapbox.MapboxState
import ca.derekellis.mapbox.rememberMapboxState
import ca.derekellis.mapbox.style.InterpolationType.Companion.exponential
import ca.derekellis.mapbox.style.expression
import ca.derekellis.mapbox.style.interpolate
import ca.derekellis.reroute.RerouteConfig
import ca.derekellis.reroute.ui.View
import ca.derekellis.reroute.utils.jsObject
import geojson.Feature
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.hsl
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

@Inject
class MapView : View<MapViewModel, MapViewEvent> {
  @Composable
  override fun Content(model: MapViewModel?, emit: (MapViewEvent) -> Unit) {
    model ?: return

    val mapState = rememberMapboxState(center = LngLat(-75.7181, 45.3922), zoom = 11.0)

    LaunchedEffect(model.targetStop) {
      val target = model.targetStop

      if (target != null) {
        val (longitude, latitude) = target.position
        mapState.flyTo(
          center = LngLat(longitude, latitude),
          zoom = 16.0,
          padding = jsObject { right = 512 },
        )
      } else {
        mapState.padding = jsObject { right = 0 }
      }
    }

    MapContent(mapState, onEvent = emit)
  }
}

@Composable
private fun MapContent(mapState: MapboxState, onEvent: (MapViewEvent) -> Unit) {
  Div {
    ca.derekellis.mapbox.MapboxMap(
      accessToken = RerouteConfig.MAPBOX_ACCESS_KEY,
      style = "mapbox://styles/mapbox/light-v10",
      state = mapState,
      // hash = true,
      containerAttrs = {
        style {
          height(100.vh)
          width(100.vw)
        }
      },
      events = {
        onClick(layers = listOf("stop-circles")) {
          val features = it.asDynamic().features.unsafeCast<Array<Feature>>()
          val target = features.firstOrNull()
          val code: String = target?.properties.asDynamic()?.code as String

          onEvent(StopClick(code))
        }
        onMouseEnter(layers = listOf("stop-circles")) {
          it.target.getCanvas().style.cursor = "pointer"
        }
        onMouseLeave(layers = listOf("stop-circles")) {
          it.target.getCanvas().style.cursor = ""
        }
      },
    ) {
      GeoJsonSource("stops", url = "/api/data/geojson") {
        CircleLayer("stop-circles") {
          circleColor(hsl(4.1, 89.6, 58.4))
          circleRadius(
            interpolate(
              exponential(2.0),
              expression("zoom"),
              12 to 2,
              15.5 to 8,
            ),
          )
        }
      }
    }
  }
}
