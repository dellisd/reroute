package ca.derekellis.reroute.server.realtime

import ca.derekellis.reroute.realtime.RealtimeMessage
import ca.derekellis.reroute.server.di.RerouteScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import me.tatarka.inject.annotations.Inject
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.seconds

/**
 * TODO: Make this more testable
 *
 *
 */
@Inject
@RerouteScope
class OcTranspoWorker(private val client: OcTranspoClient) {
  private val logger = LoggerFactory.getLogger(javaClass)
  private val streams: MutableMap<String, StreamContainer> = mutableMapOf()
  suspend fun subscribe(code: String, onUpdate: suspend (RealtimeMessage) -> Unit) {
    try {
      takeFlow(code).collectLatest { onUpdate(it) }
    } finally {
      closeFlow(code)
    }
  }

  private fun takeFlow(code: String): Flow<RealtimeMessage> {
    return when (val existing = streams[code]) {
      null -> {
        logger.info("Creating new container for {}.", code)
        streams[code] = StreamContainer(makeRequestFlow(code), AtomicInteger(0))
        streams.getValue(code).flow
      }

      else -> {
        existing.refCount.incrementAndGet()
        existing.flow
      }
    }
  }

  private fun closeFlow(code: String) {
    val existing = streams[code] ?: return
    val remainingRefs = existing.refCount.decrementAndGet()

    if (remainingRefs <= 0) {
      streams.remove(code)
      logger.info("Cleaning up container for {}.", code)
    }
  }

  private fun makeRequestFlow(code: String): Flow<RealtimeMessage> = flow {
    while (currentCoroutineContext().isActive) {
      emit(client.get(code))
      delay(LOOP_DELAY)
    }
  }

  data class StreamContainer(val flow: Flow<RealtimeMessage>, val refCount: AtomicInteger)

  private companion object {
    val LOOP_DELAY = 45.seconds
  }
}
