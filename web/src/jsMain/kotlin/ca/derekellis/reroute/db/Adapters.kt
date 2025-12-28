package ca.derekellis.reroute.db

import app.cash.sqldelight.ColumnAdapter
import com.soywiz.klock.DateTime
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.maplibre.spatialk.geojson.LineString
import org.maplibre.spatialk.geojson.toJson

object DateTimeAdapter : ColumnAdapter<DateTime, String> {
  override fun decode(databaseValue: String): DateTime = DateTime.parse(databaseValue).utc

  override fun encode(value: DateTime): String = value.toString()
}

object LineStringAdapter : ColumnAdapter<LineString, String> {
  override fun decode(databaseValue: String): LineString = LineString.fromJson(databaseValue)

  override fun encode(value: LineString): String = value.toJson()
}

class StringListAdapter(private val json: Json) : ColumnAdapter<List<String>, String> {
  override fun decode(databaseValue: String): List<String> = json.decodeFromString(ListSerializer(String.serializer()), databaseValue)

  override fun encode(value: List<String>): String = json.encodeToString(ListSerializer(String.serializer()), value)
}
