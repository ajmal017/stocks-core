package org.cerion.stocks.core.web

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.PriceRow
import org.cerion.stocks.core.model.*
import org.cerion.stocks.core.web.clients.*
import java.util.*

open class CombinedDataAPI(private val tiingoApiKey: String) : DataAPI {

    private val yahoo: YahooFinance = YahooFinance.instance
    private val tiingo: Tiingo = Tiingo(tiingoApiKey)

    override fun getPriceList(symbol: String, interval: FetchInterval, start: Date?): PriceList {
        val prices = yahoo.getPrices(symbol, interval, start)
        val list = PriceList(symbol, prices)
        list.lastUpdated = Date()

        return list
    }

    @Throws(Exception::class)
    override fun getPrices(symbol: String, interval: FetchInterval, start: Date): List<PriceRow> {
        return yahoo.getPrices(symbol, interval, start)
    }

    override fun getDividends(symbol: String): List<Dividend> {
        return yahoo.getDividends(symbol)
    }

    override fun getSymbol(symbol: String): Symbol? {
        try {
            return tiingo.getSymbol(symbol)
        } catch (ignored: Exception) {

        }

        return null
    }
}
