package ca.derekellis.reroute.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterNotNull
import me.tatarka.inject.annotations.Inject

@Inject
data class ScreenWrapper(private val presenter: Presenter<Any, Any>, private val view: View<Any, Any>) {
    private val eventsChannel = Channel<Any?>()
    private val events = eventsChannel.consumeAsFlow().filterNotNull()

    private val models = moleculeFlow(RecompositionClock.ContextClock) { presenter.produceModel(events) }
    private var model by mutableStateOf<Any?>(null)

    @Composable
    fun screen() {
        LaunchedEffect(presenter) {
            models.collect { model = it }
        }

        view.Content(model) { eventsChannel.trySend(it) }
    }
}
