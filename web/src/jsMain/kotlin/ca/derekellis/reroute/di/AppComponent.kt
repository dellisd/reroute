package ca.derekellis.reroute.di

import ca.derekellis.reroute.data.DataSource
import ca.derekellis.reroute.data.SqlJsDataSource
import ca.derekellis.reroute.db.DatabaseHelper
import ca.derekellis.reroute.RerouteApplication
import ca.derekellis.reroute.map.MapDemo
import ca.derekellis.reroute.stops.ui.Stops
import ca.derekellis.reroute.ui.Application
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@AppScope
@Component
abstract class AppComponent {
    @Provides
    protected fun dataSource(helper: DatabaseHelper): DataSource = SqlJsDataSource(helper)

    abstract val application: Application
    abstract val mapDemo: MapDemo
    abstract val stops: Stops

    abstract val rerouteApplication: RerouteApplication
}
