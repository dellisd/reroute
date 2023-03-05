package ca.derekellis.reroute.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import ca.derekellis.reroute.PresenterFactory
import ca.derekellis.reroute.ViewFactory
import ca.derekellis.reroute.di.AppScope
import ca.derekellis.reroute.home.Home
import ca.derekellis.reroute.stops.Stop
import kotlinx.browser.window
import kotlinx.coroutines.flow.MutableStateFlow
import me.tatarka.inject.annotations.Inject

@AppScope
@Inject
class AppNavigator(
    private val presenterFactory: PresenterFactory,
    private val viewFactory: ViewFactory,
) : Navigator {
    private val screens = MutableStateFlow<Screen?>(null)

    init {
        goTo(routeHash(window.location.hash))
        window.onhashchange = {
            goTo(routeHash(window.location.hash))
        }
    }

    override fun goTo(screen: Screen) {
        screens.tryEmit(screen)
    }

    // TODO: Better APIs for this, more akin the the view/presenter factories
    private fun routeHash(hash: String): Screen {
        val parts = hash.removePrefix("#").removePrefix("/").split("/")

        return when (val first = parts.getOrNull(0)) {
            "stop" -> when (val second = parts.getOrNull(1)) {
                null, "" -> Home
                else -> Stop(second)
            }
            else -> Home
        }
    }

    private fun hashRoute(screen: Screen): String? = when (screen) {
        is Stop -> "/stop/${screen.code}"
        is Home -> "/"
        else -> null
    }

    @Suppress("UNCHECKED_CAST")
    @Composable
    fun handleNavigation() {
        val screen by screens.collectAsState()
        if (screen == null) return

        DisposableEffect(screen) {
            val hash = hashRoute(screen!!)
            window.location.hash = hash ?: ""
            onDispose {  }
        }

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
