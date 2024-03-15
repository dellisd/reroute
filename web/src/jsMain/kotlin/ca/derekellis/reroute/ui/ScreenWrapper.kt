package ca.derekellis.reroute.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.tatarka.inject.annotations.Inject

@Inject
class ScreenWrapper(
  private val presenter: Presenter<Any, Any>,
  private val view: View<Any, Any>,
) {
  @Composable
  fun screen() {
    val triggerFlow = remember { MutableSharedFlow<Any>(replay = 1) }
    val listenerFlow = remember { triggerFlow.asSharedFlow() }

    val model = presenter.produceModel(listenerFlow)

    view.Content(model) { triggerFlow.tryEmit(it) }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class.js != other::class.js) return false

    other as ScreenWrapper

    if (presenter != other.presenter) return false
    return view == other.view
  }

  override fun hashCode(): Int {
    var result = presenter.hashCode()
    result = 31 * result + view.hashCode()
    return result
  }
}
