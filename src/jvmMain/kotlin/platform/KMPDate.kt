package org.cerion.stocks.core.platform

import java.text.SimpleDateFormat
import java.util.*

actual class KMPDate actual constructor(year: Int, month: Int, date: Int) : Comparable<KMPDate> {
    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val TODAY: KMPDate
            get() = KMPDate(Date())
    }

    private var _date: Date = Date(year - 1900, month - 1, date)

    // Ideally this should not be used but some cases where its needed
    constructor(date: Date) : this(date.year + 1900, date.month + 1, date.date)

    actual fun toISOString(): String = dateFormat.format(_date) // YYYY-MM-DD
    override fun toString(): String = toISOString()

    actual val time: Long
        get() = _date.time

    // TODO equals should ignore time, depending how it was constructed the same dates may not return true
    actual override fun equals(other: Any?): Boolean {
        if (other is KMPDate)
            return _date.compareTo(other._date) == 0

        return false
    }

    override fun hashCode(): Int = _date.hashCode()
    override fun compareTo(other: KMPDate): Int = _date.compareTo(other._date)

    // TODO might might be the same as 'day', merge to single that is enum return type
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

    // TODO add diff function that returns integer for days between
    // TODO add add() function to return new date X days back/ahead
}