package ca.derekellis.reroute.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import ca.derekellis.reroute.PresenterFactory
import ca.derekellis.reroute.ViewFactory
import ca.derekellis.reroute.di.AppScope
import kotlinx.coroutines.flow.MutableStateFlow
import me.tatarka.inject.annotations.Inject

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

    @Suppress("UNCHECKED_CAST")
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
