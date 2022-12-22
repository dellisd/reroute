package ca.derekellis.reroute.home

import androidx.compose.runtime.Composable
import ca.derekellis.reroute.ui.Presenter
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class HomePresenter(private val args: Home) : Presenter<Unit, Unit> {
    @Composable
    override fun produceModel(events: Flow<Unit>) = Unit
}
