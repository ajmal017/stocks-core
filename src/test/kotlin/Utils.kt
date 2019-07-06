package org.cerion.stocklist

import org.cerion.stocklist.model.Dividend
import java.util.*

object Utils {

    fun generateList(size: Int): PriceList {
        val prices = ArrayList<Price>()
        for (i in 0 until size)
            prices.add(Price(Date(), i.toFloat(), i.toFloat(), i.toFloat(), i.toFloat(), i.toFloat()))

        return PriceList("TEST", prices)
    }

    fun getDividends(vararg values: Float): List<Dividend> {
        val calendar = Calendar.getInstance()
        val result = ArrayList<Dividend>()

        for (v in values) {
            val d = Dividend(calendar.time, v)
            result.add(d)

            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }

        return result
    }

    fun getDate(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -daysAgo)
        return calendar.time
    }
}
