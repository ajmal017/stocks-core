package org.cerion.stocks.core.platform

expect class KMPDate(year: Int, month: Int, date: Int) : Comparable<KMPDate> {
    fun toISOString(): String
    val time: Long
    val dayOfWeek: Int
    val year: Int
    val date: Int
    val day: Int
    val month: Int

    override fun equals(other: Any?): Boolean
}