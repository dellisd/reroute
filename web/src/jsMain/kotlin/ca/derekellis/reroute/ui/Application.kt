package ca.derekellis.reroute.ui

import androidx.compose.runtime.Composable
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.web.dom.Div

typealias Application = @Composable () -> Unit

typealias SearchScreenWrapper = ScreenWrapper

/**
 * TODO: Don't pass searchViewModel like this
 */
@Inject
@Composable
fun Application(searchScreenWrapper: SearchScreenWrapper) {
  Div(attrs = {
    classes(AppStylesheet.overlayContainer)
  }) {
    searchScreenWrapper.screen()
  }
}
