package ca.derekellis.reroute.db

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDateTime

object LocalDateTimeAdapter : ColumnAdapter<LocalDateTime, String> {
    override fun decode(databaseValue: String): LocalDateTime = LocalDateTime.parse(databaseValue)

    override fun encode(value: LocalDateTime): String = value.toString()
}
