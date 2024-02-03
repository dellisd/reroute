package ca.derekellis.reroute.di

import ca.derekellis.reroute.PresenterFactory
import ca.derekellis.reroute.RerouteApplication
import ca.derekellis.reroute.ViewFactory
import ca.derekellis.reroute.data.DataSource
import ca.derekellis.reroute.data.SqlJsDataSource
import ca.derekellis.reroute.db.DatabaseHelper
import ca.derekellis.reroute.search.Search
import ca.derekellis.reroute.ui.AppNavigator
import ca.derekellis.reroute.ui.Application
import ca.derekellis.reroute.ui.Navigator
import ca.derekellis.reroute.ui.Presenter
import ca.derekellis.reroute.ui.ScreenWrapper
import ca.derekellis.reroute.ui.SearchScreenWrapper
import ca.derekellis.reroute.ui.View
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.w3c.dom.Worker

@AppScope
@Component
abstract class AppComponent {
  @Provides
  @AppScope
  protected fun dataSource(helper: DatabaseHelper): DataSource = SqlJsDataSource(helper)

  abstract val application: Application

  abstract val rerouteApplication: RerouteApplication

  abstract val appNavigator: AppNavigator

  @Provides
  @AppScope
  protected fun navigator(navigator: AppNavigator): Navigator = navigator

  abstract val viewFactory: ViewFactory
  abstract val presenterFactory: PresenterFactory

  @Provides
  @AppScope
  protected fun searchScreenWrapper(viewFactory: ViewFactory, presenterFactory: PresenterFactory): SearchScreenWrapper =
    ScreenWrapper(
      presenterFactory.createPresenter(appNavigator, Search) as Presenter<Any, Any>,
      viewFactory.createView(Search) as View<Any, Any>,
    )

  @Provides
  @AppScope
  protected fun provideWorker(): Worker =
    //language=JavaScript
    js("""new Worker(new URL("./worker.js", import.meta.url))""").unsafeCast<Worker>()

  @Provides
  @AppScope
  protected fun provideHttpClient(): HttpClient = HttpClient(Js) {
    install(ContentNegotiation) {
      json()
    }
    install(WebSockets) {
      contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
  }
}
