package io.github.dellisd.reroute.di

import io.github.dellisd.reroute.RerouteApplication
import io.github.dellisd.reroute.data.DataSource
import io.github.dellisd.reroute.data.SqlJsDataSource
import io.github.dellisd.reroute.db.DatabaseHelper
import io.github.dellisd.reroute.map.MapDemo
import io.github.dellisd.reroute.ui.Application
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@AppScope
@Component
abstract class AppComponent {
    @Provides
    protected fun dataSource(helper: DatabaseHelper): DataSource = SqlJsDataSource(helper)

    abstract val application: Application
    abstract val mapDemo: MapDemo

    abstract val rerouteApplication: RerouteApplication
}
