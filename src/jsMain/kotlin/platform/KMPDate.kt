package org.cerion.stocks.core.platform


actual class KMPDate actual constructor(year: Int, month: Int, date: Int) : Comparable<KMPDate> {
    override fun compareTo(other: KMPDate): Int {
        TODO("Not yet implemented")
    }

    actual fun toISOString(): String {
        TODO("Not yet implemented")
    }

    actual val time: Long
        get() = TODO("Not yet implemented")
    actual val dayOfWeek: DayOfWeek
        get() = TODO("Not yet implemented")
    actual val year: Int
        get() = TODO("Not yet implemented")
    actual val date: Int
        get() = TODO("Not yet implemented")
    actual val month: Int
        get() = TODO("Not yet implemented")

    actual fun add(days: Int): KMPDate {
        TODO("Not yet implemented")
    }

    actual fun diff(other: KMPDate): Int {
        TODO("Not yet implemented")
    }

    actual companion object {
        actual val TODAY: KMPDate
            get() = TODO("Not yet implemented")
    }

}