package ca.derekellis.reroute.server.realtime

import ca.derekellis.reroute.realtime.NextRoute
import ca.derekellis.reroute.realtime.NextTrip
import ca.derekellis.reroute.realtime.RealtimeMessage
import ca.derekellis.reroute.server.config.LoadedServerConfig
import ca.derekellis.reroute.server.di.RerouteScope
import io.github.dellisd.spatialk.geojson.dsl.lngLat
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.http.isSuccess
import kotlinx.datetime.toKotlinLocalTime
import me.tatarka.inject.annotations.Inject
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import kotlin.time.Duration.Companion.seconds

@Inject
@RerouteScope
class OcTranspoClient(
  private val config: LoadedServerConfig,
  private val httpClient: HttpClient,
) {
  private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

  suspend fun get(code: String): RealtimeMessage {
    val credentials = requireNotNull(config.ocTranspo) { "No OC Transpo API credentials loaded." }
    val response =
      httpClient.get("https://api.octranspo1.com/v2.0/GetNextTripsForStopAllRoutes?appID=${credentials.appId}&apiKey=${credentials.apiKey}&stopNo=$code&format=xml")
    val responseTime = LocalTime.now(ZoneId.of("America/Montreal"))

    if (!response.status.isSuccess()) {
      throw ServerResponseException(response, response.readBytes().toString(Charsets.UTF_8))
    }

    return buildResultFromResponse(code, response.readBytes(), responseTime)
  }

  private fun buildResultFromResponse(code: String, xmlBytes: ByteArray, time: LocalTime): RealtimeMessage {
    val convertedTime = time.toKotlinLocalTime()
    val xPath = XPathFactory.newInstance().newXPath()
    val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val document = documentBuilder.parse(ByteArrayInputStream(xmlBytes))

    val responseRoot = xPath.evaluate(
      "/Envelope/Body/GetRouteSummaryForStopResponse/GetRouteSummaryForStopResult",
      document,
      XPathConstants.NODE,
    ) as? Element
      ?: return RealtimeMessage(code, emptyList(), convertedTime) // Return error if node ended up not existing

    // Check for any errors returned from the API. TODO : Check error conditions
    val errorCode = (xPath.evaluate("./Error", responseRoot, XPathConstants.STRING) as? String)?.toIntOrNull()
    if (errorCode != null && errorCode != 0) {
      return RealtimeMessage(code, emptyList(), convertedTime)
    }

    val routeNodes = xPath.evaluate("./Routes/Route", responseRoot, XPathConstants.NODESET) as NodeList
    val routes = buildList {
      routeNodes.forEach<Element> { element ->
        add(buildRouteFromElement(element, xPath, time))
      }
    }

    return RealtimeMessage(code, routes, convertedTime)
  }

  /**
   * Builds a [NextRoute] object given the Route element from the XML response.
   * Extracts all the required details including this route's list of [NextTrip]s.
   *
   * @param element The DOM Element for this route from the API response.
   */
  private fun buildRouteFromElement(element: Element, xPath: XPath, time: LocalTime): NextRoute {
    val number = xPath.evaluate("./RouteNo", element, XPathConstants.STRING) as String
    val directionId = (xPath.evaluate("./DirectionID", element, XPathConstants.STRING) as String).toInt()
    val direction = xPath.evaluate("./Direction", element, XPathConstants.STRING) as String
    val heading = xPath.evaluate("./RouteHeading", element, XPathConstants.STRING) as String
    val tripsElements = xPath.evaluate("./Trips/Trip", element, XPathConstants.NODESET) as NodeList

    val trips = buildList {
      tripsElements.forEach<Element> { element -> add(buildTripFromElement(element, xPath, time)) }
    }

    return NextRoute(number, directionId, direction, heading, trips)
  }

  /**
   * Builds a [NextTrip] object given the Trip element from the XML response.
   * Extracts all the required details including a readable [NextTrip.busType] string and [NextTrip.hasBikeRack].
   *
   * TODO: At this time, this function does not calculate the trip's [NextTrip.punctuality].
   *
   * @param element The DOM Element for this trip from the API response.
   */
  private fun buildTripFromElement(element: Element, xPath: XPath, time: LocalTime): NextTrip {
    val tripDestination = xPath.evaluate("./TripDestination", element, XPathConstants.STRING) as String
    val tripStartTime = xPath.evaluate("./TripStartTime", element, XPathConstants.STRING) as String
    val adjustedScheduleTime =
      (xPath.evaluate("./AdjustedScheduleTime", element, XPathConstants.STRING) as String).toLong()
    val adjustmentAge = (xPath.evaluate("./AdjustmentAge", element, XPathConstants.STRING) as String).toFloat()
    val lastTripOfSchedule =
      (xPath.evaluate("./LastTripOfSchedule", element, XPathConstants.STRING) as String).toLowerCase() == "1"
    val busTypeText = xPath.evaluate("./BusType", element, XPathConstants.STRING) as String
    val latitudeText = xPath.evaluate("./Latitude", element, XPathConstants.STRING) as String
    val longitudeText = xPath.evaluate("./Longitude", element, XPathConstants.STRING) as String
    val gpsText = xPath.evaluate("./GPSSpeed", element, XPathConstants.STRING) as String

    val latitude = latitudeText.toDoubleOrNull()
    val longitude = longitudeText.toDoubleOrNull()
    val position = if (latitude != null && longitude != null) lngLat(longitude, latitude) else null

    val computedStartTime = tripStartTime.takeIf(String::isNotEmpty)?.let { LocalTime.parse(it, timeFormat) }
    val computedScheduleTime = time + Duration.ofMinutes(adjustedScheduleTime)
    val computedAge = (adjustmentAge * 60).toLong().seconds

    return NextTrip(
      tripDestination,
      computedStartTime?.toKotlinLocalTime(),
      computedScheduleTime.toKotlinLocalTime(),
      computedAge,
      lastTripOfSchedule,
      busType = getBusTypeFromString(busTypeText),
      position = position,
      gpsSpeed = gpsText.toFloatOrNull(),
      hasBikeRack = busTypeText.contains("B"),
      punctuality = 0,
    )
  }

  /**
   * Converts the "BusType" string returned by the OC Transpo API into a readable letter code.
   *
   * Example of OC Transpo's strings:
   * "6EB - 60", "4E - DEH", "4L - DEH", "4LB - IN", "DD - DEH", etc.
   *
   * The symbols mean the following:
   * * 4 or 40 = 40-foot bus
   * * 6 or 60 = 60-foot bus
   * * 4 and 6 = Either a 40 or 60 foot bus
   * * DD = Double Decker
   * * E, L, A, EA = Low floor easy access
   * * B = Bike Rack
   * * DEH = Diesel Electric Hybrid
   * * IN = New Flyer Inviro (40-foot bus)
   * * ON = Supposed to indicate an Orion Bus but apparently indicates a Nova Bus currently.
   *
   * Note that in the case of DD-DEH, this tends to signify an "extra". And extra is just an extra bus that is
   * assigned by OC Transpo during rush hours to handle extra trips. More often than not, it is not a Double Decker.
   *
   * @param typeString The "BusType" string from the OC Transpo API.
   * @return The readable letter code for that bus type. Either "S", "L", "H", or "DD".
   */
  private fun getBusTypeFromString(typeString: String): String {
    return when {
      typeString.contains("ON") -> "N"
      typeString.contains("H") && !typeString.contains("DD") -> "H"
      typeString.contains("6") -> "L"
      typeString.contains("4") -> "S"
      typeString.contains("DD") && !typeString.contains("DEH") -> "DD"
      else -> ""
    }
  }

  /**
   * Subscript operator for the [NodeList] class.
   * More convenient to use than the [NodeList.item] method.
   *
   * @param i The index into the collection.
   */
  private operator fun NodeList.get(i: Int) = item(i)

  @Suppress("UNCHECKED_CAST")
  private inline fun <T> NodeList.forEach(block: (T) -> Unit) {
    for (i in 0 until length) {
      block(get(i) as T)
    }
  }
}
