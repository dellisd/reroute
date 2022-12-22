package ca.derekellis.reroute.ui

import androidx.compose.runtime.Composable

interface View<ViewModel, ViewEvent> {
    @Composable
    fun Content(model: ViewModel?, emit: (ViewEvent) -> Unit)
}
