package io.github.dellisd.reroute

import io.github.dellisd.reroute.data.ResourceLoader
import io.github.dellisd.reroute.di.AppScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@AppScope
@Inject
class RerouteApplication(private val resourceLoader: ResourceLoader) {
    private val appScope = CoroutineScope(Dispatchers.Main)

    fun init() {
        appScope.launch {
            resourceLoader.loadStopsToDatabase()
        }
    }
}
