package ca.derekellis.reroute.ui

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

interface Presenter<ViewModel, ViewEvent> {
    @Composable
    fun produceModel(events: Flow<ViewEvent>): ViewModel
}
