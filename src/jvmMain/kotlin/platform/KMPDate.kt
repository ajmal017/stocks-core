package org.cerion.stocks.core.platform

import java.text.SimpleDateFormat
import java.util.*

actual class KMPDate actual constructor(year: Int, month: Int, date: Int) : Comparable<KMPDate> {
    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    }

    // TODO many of the date constructors in unit tests can be changed to take the primary constructor
    @Deprecated("Need to make parser for ISO date format / converter for room database")
    constructor(date: Date) : this(date.year + 1900, date.month + 1, date.date)

    constructor() : this(Date()) // For unit tests, change or use a NOW in companion

    private var _date: Date = Date(year - 1900, month - 1, date)

    actual fun toISOString(): String = dateFormat.format(_date)

    actual val time: Long
        get() = _date.time

    actual override fun equals(other: Any?): Boolean { // TODO should ignore time, add test for that
        if (other is KMPDate)
            return _date.compareTo(other._date) == 0

        return false
    }

    override fun hashCode(): Int = _date.hashCode()
    override fun compareTo(other: KMPDate): Int = _date.compareTo(other._date)

    actual val dayOfWeek: Int
        get() {
            val c = Calendar.getInstance()
            c.time = _date
            return c.get(Calendar.DAY_OF_WEEK)
        }

    actual val year: Int
        get() = _date.year

    actual val date: Int
        get() = _date.date

    actual val day: Int
        get() = _date.day

    val jvmDate: Date = _date
}