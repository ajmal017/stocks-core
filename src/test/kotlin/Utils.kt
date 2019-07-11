package org.cerion.stocklist

import org.cerion.stocklist.model.Dividend
import org.cerion.stocklist.web.api.YahooFinance
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

object Utils {

    val sP500TestData: PriceList by lazy {
        val data = resourceToString("sp500_2000-2015.csv")
        PriceList("^GSPC", YahooFinance.getPricesFromTable(data))
    }

    fun resourceToString(fileName: String): String {
        val classloader = Thread.currentThread().contextClassLoader
        val inputStream = classloader.getResourceAsStream(fileName)

        val isr = InputStreamReader(inputStream)
        val br = BufferedReader(isr)
        val sb = StringBuffer()
        for(line in br.lines())
            sb.append(line + "\r\n")

        return sb.toString()
    }

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
