package ca.derekellis.reroute.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@Composable
inline fun <T> CollectEffect(flow: Flow<T>, crossinline block: CoroutineScope.(T) -> Unit) {
  LaunchedEffect(flow) {
    flow.collect { block(it) }
  }
}
