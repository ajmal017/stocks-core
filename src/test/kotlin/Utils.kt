package org.cerion.stocklist

import org.cerion.stocklist.model.Dividend
import org.cerion.stocklist.web.api.YahooFinance
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

object Utils {

    //grab the contents at the URL
    // Nothing
    val sP500TestData: PriceList by lazy {
        val classloader = Thread.currentThread().contextClassLoader
        //val res = classloader.getResource(".")
        val inputStream = classloader.getResourceAsStream("sp500_2000-2015.csv")

        val isr = InputStreamReader(inputStream)
        val br = BufferedReader(isr)
        val sb = StringBuffer()
        for(line in br.lines())
            sb.append(line + "\r\n")

        val data = sb.toString()
        PriceList("^GSPC", YahooFinance.getPricesFromTable(data))
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
