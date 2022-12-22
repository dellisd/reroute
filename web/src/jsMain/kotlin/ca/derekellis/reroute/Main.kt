package ca.derekellis.reroute

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.softwork.routingcompose.HashRouter
import ca.derekellis.reroute.di.AppComponent
import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.di.create
import ca.derekellis.reroute.ui.AppStylesheet
import ca.derekellis.reroute.ui.Navigator
import ca.derekellis.reroute.ui.Presenter
import ca.derekellis.reroute.ui.Screen
import ca.derekellis.reroute.ui.View
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

external fun require(module: String): dynamic

data class ScreenWrapper(private val presenter: Presenter<Any, Any>, private val view: View<Any, Any>) {

    private val events = MutableStateFlow<Any?>(null)

    private val models =
        moleculeFlow(RecompositionClock.ContextClock) { presenter.produceModel(events.filterNotNull()) }
    private var model by mutableStateOf<Any?>(null)

    @Composable
    fun screen() {
        LaunchedEffect(presenter) {
            models.collect { model = it }
        }

        view.Content(model) { events.tryEmit(it) }
    }
}

@AppScope
@Inject
class AppNavigator(
    private val presenterFactory: PresenterFactory,
    private val viewFactory: ViewFactory,
) : Navigator {
    private val screens = MutableStateFlow<Screen?>(null)
    override fun goTo(screen: Screen) {
        screens.tryEmit(screen)
    }

    @Composable
    fun handleNavigation() {
        val screen by screens.collectAsState()
        if (screen == null) return

        screen?.let {
            val wrapper = remember(it) {
                ScreenWrapper(
                    presenterFactory.createPresenter(it) as Presenter<Any, Any>,
                    viewFactory.createView(it) as View<Any, Any>,
                )
            }

            wrapper.screen()
        }
    }
}

fun main() {
    require("mapbox-gl/dist/mapbox-gl.css")
    val component = AppComponent::class.create()

    component.rerouteApplication.init()

    renderComposable(rootElementId = "root") {
        Style(AppStylesheet)

        HashRouter(initPath = "/") {
            component.mapDemo()
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
