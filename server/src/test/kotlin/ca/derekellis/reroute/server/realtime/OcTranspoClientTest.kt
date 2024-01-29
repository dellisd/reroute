package ca.derekellis.reroute.server.realtime

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.messageContains
import ca.derekellis.reroute.server.config.LoadedServerConfig
import ca.derekellis.reroute.server.config.OcTranspoCredentials
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.Test
import kotlin.io.path.Path
import kotlin.time.Duration.Companion.minutes

class OcTranspoClientTest {
  private val config = LoadedServerConfig(
    dataPath = Path(""),
    ocTranspo = OcTranspoCredentials("", ""),
  )

  private fun makeClient(engine: MockEngine): OcTranspoClient = OcTranspoClient(config, HttpClient(engine))

  @Test
  fun `basic endpoint response is parsed correctly`() = runTest {
    val engine = MockEngine {
      respond(
        DEFAULT,
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "text/html"),
      )
    }

    val client = makeClient(engine)
    val result = client.get("4106")

    assertThat(result.stop).isEqualTo("4106")

    val route = result.routes.first()
    assertThat(route.number).isEqualTo("6")
    assertThat(route.heading).isEqualTo("Rockcliffe")
    assertThat(route.directionId).isEqualTo(1)
    assertThat(route.trips).hasSize(2)

    val trip = route.trips.first()
    assertThat(trip.destination).isEqualTo("Rockcliffe")
    assertThat(trip.startTime).isEqualTo(LocalTime(22, 49))
    // TODO: Test adjusted schedule time conversion with a custom clock
    assertThat(trip.adjustmentAge).isEqualTo((-1).minutes)
  }

  @Test
  fun `http error throws error`() = runTest {
    val engine = MockEngine {
      respond("", HttpStatusCode.BadGateway)
    }

    val client = makeClient(engine)
    assertFailure { client.get("0000") }
      .isInstanceOf<ServerResponseException>()
      .messageContains("502 Bad Gateway")
  }

  companion object {
    private val DEFAULT = """
      <?xml
      version="1.0" encoding="utf-8"?>
      <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <soap:Body>
              <GetRouteSummaryForStopResponse xmlns="http://octranspo.com">
                  <GetRouteSummaryForStopResult>
                      <StopNo>4106</StopNo>
                      <StopDescription>BANK / ERIE</StopDescription>
                      <Error/>
                      <Routes>
                          <Route>
                              <RouteNo>6</RouteNo>
                              <RouteHeading>Rockcliffe</RouteHeading>
                              <DirectionID>1</DirectionID>
                              <Direction/>
                              <Error/>
                              <Trips>
                                  <Trip>
                                      <TripDestination>Rockcliffe</TripDestination>
                                      <TripStartTime>22:49</TripStartTime>
                                      <AdjustedScheduleTime>13</AdjustedScheduleTime>
                                      <AdjustmentAge>-1</AdjustmentAge>
                                      <LastTripOfSchedule>false</LastTripOfSchedule>
                                      <BusType/>
                                      <Longitude/>
                                      <Latitude/>
                                      <GPSSpeed/>
                                  </Trip>
                                  <Trip>
                                      <TripDestination>Rockcliffe</TripDestination>
                                      <TripStartTime>23:18</TripStartTime>
                                      <AdjustedScheduleTime>42</AdjustedScheduleTime>
                                      <AdjustmentAge>-1</AdjustmentAge>
                                      <LastTripOfSchedule>false</LastTripOfSchedule>
                                      <BusType/>
                                      <Longitude/>
                                      <Latitude/>
                                      <GPSSpeed/>
                                  </Trip>
                              </Trips>
                          </Route>
                      </Routes>
                  </GetRouteSummaryForStopResult>
              </GetRouteSummaryForStopResponse>
          </soap:Body>
      </soap:Envelope>
    """.trimIndent()
  }
}
