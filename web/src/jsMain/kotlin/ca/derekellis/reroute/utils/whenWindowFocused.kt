package ca.derekellis.reroute.utils

import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.events.Event

suspend fun whenWindowFocused(block: suspend () -> Unit): Nothing = coroutineScope {
  var job: Job? = null
  val focusListener: (Event) -> Unit = {
    job?.cancel()
    job = launch { block() }
  }
  val blurListener: (Event) -> Unit = {
    job?.cancel()
  }

  try {
    window.addEventListener("focus", focusListener)
    window.addEventListener("blur", blurListener)

    awaitCancellation()
  } finally {
    window.removeEventListener("focus", focusListener)
    window.removeEventListener("blur", blurListener)
  }
}
