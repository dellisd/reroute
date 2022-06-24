package ca.derekellis.reroute.db

import app.cash.sqldelight.ColumnAdapter
import com.soywiz.klock.DateTime

object DateTimeAdapter : ColumnAdapter<DateTime, String> {
    override fun decode(databaseValue: String): DateTime = DateTime.parse(databaseValue).utc

    override fun encode(value: DateTime): String = value.toString()
}
