package ca.derekellis.reroute

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import app.softwork.routingcompose.HashRouter
import ca.derekellis.reroute.di.AppComponent
import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.di.create
import ca.derekellis.reroute.ui.AppStylesheet
import ca.derekellis.reroute.ui.Navigator
import ca.derekellis.reroute.ui.Presenter
import ca.derekellis.reroute.ui.Screen
import ca.derekellis.reroute.ui.View
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

external fun require(module: String): dynamic

@AppScope
@Inject
class AppNavigator(
    private val presenterFactory: PresenterFactory,
    private val viewFactory: ViewFactory,
) : Navigator {
    private val screens = MutableStateFlow<Screen?>(null)
    override fun goTo(screen: Screen) {
        val success = screens.tryEmit(screen)
        println("Trying to go to $screen, $success")
    }

    @Composable
    fun handleNavigation() {
        val screen by screens.collectAsState()
        if (screen == null) return

        val scope = rememberCoroutineScope()

        screen?.let {
            val presenter = remember(screen) { presenterFactory.createPresenter(it) as Presenter<Any, Any> }
            val view = remember(screen) { viewFactory.createView(it) as View<Any, Any> }
            val events = remember(screen) { MutableSharedFlow<Any>() }

            val model by remember(screen) {
                scope.launchMolecule(RecompositionClock.ContextClock) {
                    presenter.produceModel(events)
                }
            }.collectAsState()

            view.Content(model, events::tryEmit)
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
