package ca.derekellis.reroute

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import app.softwork.routingcompose.HashRouter
import ca.derekellis.reroute.di.AppComponent
import ca.derekellis.reroute.di.create
import ca.derekellis.reroute.map.Map
import ca.derekellis.reroute.ui.AppStylesheet
import ca.derekellis.reroute.ui.Presenter
import ca.derekellis.reroute.ui.ScreenWrapper
import ca.derekellis.reroute.ui.View
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

external fun require(module: String): dynamic

fun main() {
    require("mapbox-gl/dist/mapbox-gl.css")
    val component = AppComponent::class.create()

    component.rerouteApplication.init()

    renderComposable(rootElementId = "root") {
        Style(AppStylesheet)

        HashRouter(initPath = "/") {
            MapSection(component.viewFactory, component.presenterFactory)
            component.application()

//            route("/stops") {
//                string { code ->
//                    component.stops(code)
//                }
//            }
            component.appNavigator.handleNavigation()
        }
    }
}

@Composable
fun MapSection(
    viewFactory: ViewFactory,
    presenterFactory: PresenterFactory,
) {
    val wrapper = remember {
        ScreenWrapper(
            presenterFactory.createPresenter(Map) as Presenter<Any, Any>,
            viewFactory.createView(Map) as View<Any, Any>
        )
    }

    wrapper.screen()
}
