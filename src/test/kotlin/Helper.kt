package org.cerion.stocklist

import org.cerion.stocklist.web.api.YahooFinance
import java.io.BufferedReader
import java.io.InputStreamReader

object Helper {

    //grab the contents at the URL
    // Nothing
    val sP500TestData: PriceList?
        get() {
            val classloader = Thread.currentThread().contextClassLoader
            //val res = classloader.getResource(".")
            val inputStream = classloader.getResourceAsStream("sp500_2000-2015.csv")

            try {
                val isr = InputStreamReader(inputStream)
                val br = BufferedReader(isr)
                val sb = StringBuffer()
                for(line in br.lines())
                    sb.append(line + "\r\n")

                val data = sb.toString()
                return PriceList("^GSPC", YahooFinance.getPricesFromTable(data))
            } catch (e: Exception) {
            }

            return null
        }
}
