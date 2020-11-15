package org.cerion.stocks.core.platform

import kotlin.js.Date

val MS_PER_DAY = 1000 * 60 * 60 * 24

actual class KMPDate actual constructor(year: Int, month: Int, date: Int) : Comparable<KMPDate> {

    private var _date: Date = Date(year, month - 1, date)
    constructor(date: Date) : this(date.getFullYear(), date.getMonth() + 1, date.getDate())

    override fun compareTo(other: KMPDate): Int {
        return _date.getTime().compareTo(other._date.getTime())
    }

    actual fun toISOString(): String = _date.toISOString().substring(0, 10)

    actual val time: Long
        get() = TODO("Not yet implemented")

    actual val dayOfWeek: DayOfWeek
        get() {
            return when (_date.getDay()) {
                0 -> DayOfWeek.SUNDAY
                1 -> DayOfWeek.MONDAY
                2 -> DayOfWeek.TUESDAY
                3 -> DayOfWeek.WEDNESDAY
                4 -> DayOfWeek.THURSDAY
                5 -> DayOfWeek.FRIDAY
                6 -> DayOfWeek.SATURDAY
                else -> throw RuntimeException()
            }
        }

    actual val year: Int
        get() = _date.getFullYear()

    actual val date: Int
        get() = _date.getDate()

    actual val month: Int
        get() = _date.getMonth()

    actual fun add(days: Int): KMPDate {
        val date = Date(_date.toString())
        date.asDynamic().setDate(date.getDate() + days)
        return KMPDate(date)
    }

    actual fun diff(other: KMPDate): Int {
        val utc1 = Date.UTC(_date.getFullYear(), _date.getMonth(), _date.getDate())
        val utc2 = Date.UTC(other._date.getFullYear(), other._date.getMonth(), other._date.getDate())

        return ((utc1 - utc2) / MS_PER_DAY).toInt()
    }

    actual companion object {
        actual val TODAY: KMPDate
            get() = KMPDate(Date())
    }
}