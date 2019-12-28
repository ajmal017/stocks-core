package org.cerion.stocks.core.web

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.PriceRow
import org.cerion.stocks.core.model.Dividend
import org.cerion.stocks.core.model.Interval
import org.cerion.stocks.core.model.Quote
import org.cerion.stocks.core.model.Symbol
import org.cerion.stocks.core.web.clients.*
import java.util.*

class CombinedDataAPI : DataAPI {

    private val yahoo: YahooFinance = YahooFinance.instance
    private val google: GoogleFinance = GoogleFinance()
    private val tiingo: Tiingo = Tiingo()

    override fun getPriceList(symbol: String, interval: Interval, start: Date): PriceList {
        val prices = getPrices(symbol, interval, start)
        val list = PriceList(symbol, prices)
        list.lastUpdated = Date()

        return list
    }

    @Throws(Exception::class)
    override fun getPrices(symbol: String, interval: Interval, start: Date): List<PriceRow> {
        return yahoo.getPrices(symbol, interval, start)
    }

    override fun getDividends(symbol: String): List<Dividend> {
        return yahoo.getDividends(symbol)
    }

    override fun getSymbols(symbols: Set<String>): List<Symbol> {
        throw UnsupportedOperationException()
    }

    override fun getSymbol(symbol: String): Symbol? {
        try {
            return tiingo.getSymbol(symbol)
        } catch (ignored: Exception) {

        }

        return null
    }

    override fun getQuotes(symbols: Set<String>): Map<String, Quote> {
        throw UnsupportedOperationException()
    }

    override fun getQuote(symbol: String): Quote? {
        return google.getQuote(symbol)
    }
}
