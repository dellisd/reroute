package ca.derekellis.reroute.db

import app.cash.sqldelight.ColumnAdapter
import com.soywiz.klock.DateTime
import io.github.dellisd.spatialk.geojson.LineString

object DateTimeAdapter : ColumnAdapter<DateTime, String> {
    override fun decode(databaseValue: String): DateTime = DateTime.parse(databaseValue).utc

    override fun encode(value: DateTime): String = value.toString()
}

object LineStringAdapter : ColumnAdapter<LineString, String> {
    override fun decode(databaseValue: String): LineString = LineString.fromJson(databaseValue)

    override fun encode(value: LineString): String = value.json()
}
