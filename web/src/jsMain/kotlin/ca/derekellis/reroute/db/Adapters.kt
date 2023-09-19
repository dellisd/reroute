package ca.derekellis.reroute.db

import app.cash.sqldelight.ColumnAdapter
import com.soywiz.klock.DateTime
import io.github.dellisd.spatialk.geojson.LineString
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

object DateTimeAdapter : ColumnAdapter<DateTime, String> {
  override fun decode(databaseValue: String): DateTime = DateTime.parse(databaseValue).utc

  override fun encode(value: DateTime): String = value.toString()
}

object LineStringAdapter : ColumnAdapter<LineString, String> {
  override fun decode(databaseValue: String): LineString = LineString.fromJson(databaseValue)

  override fun encode(value: LineString): String = value.json()
}

class StringListAdapter(private val json: Json) : ColumnAdapter<List<String>, String> {
  override fun decode(databaseValue: String): List<String> {
    return json.decodeFromString(ListSerializer(String.serializer()), databaseValue)
  }

  override fun encode(value: List<String>): String {
    return json.encodeToString(ListSerializer(String.serializer()), value)
  }
}
