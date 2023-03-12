package ca.derekellis.reroute

import ca.derekellis.reroute.db.DatabaseHelper
import ca.derekellis.reroute.di.AppScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@AppScope
@Inject
class RerouteApplication(private val databaseHelper: DatabaseHelper) {
    private val appScope = CoroutineScope(Dispatchers.Main)

    fun init() {
        appScope.launch {
            databaseHelper.initDatabase()
        }
    }
}
